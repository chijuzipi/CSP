package org.cspapplier;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.lang3.StringUtils;

import org.cspapplier.mongo.PageJsonColl;
import org.littleshoot.proxy.HttpProxyServerBootstrap;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.extras.SelfSignedMitmManager;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.impl.ProxyUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.buffer.Unpooled.buffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


/**
 * Launcher.java
 *
 * Main class to start the proxy
 *
 */

public class Launcher {
    private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);
    private static final String OPTION_DNSSEC = "dnssec";
    private static final String OPTION_PORT = "port";
    private static final String OPTION_HELP = "help";
    private static final String OPTION_MITM = "mitm";
    private static final String OPTION_HTTP = "http";
    private static final String OPTION_FILE= "file";
    private static final String OPTION_DB= "db";

    /**
     * Starts the proxy from the command line.
     *
     * @param args
     *            Any command line arguments.
     */
    public static void main(final String... args) {
        LOG.info("Running LittleProxy with args: {}", Arrays.asList(args));
        final Options options = new Options();
        options.addOption(null, OPTION_DNSSEC, true,
                "Request and verify DNSSEC signatures.");
        options.addOption(null, OPTION_PORT, true, "Run on the specified port.");
        options.addOption(null, OPTION_HELP, false,
                "Display command line help.");
        options.addOption(null, OPTION_MITM, false, "Run as man in the middle.");
        options.addOption(null, OPTION_HTTP, false, "Run on the specified HTTP server.");
        options.addOption(null, OPTION_FILE, false, "Save files on the specified location.");
        options.addOption(null, OPTION_DB, false, "Save templates in the specified database.");

        final CommandLineParser parser = new PosixParser();
        final CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            if (cmd.getArgs().length > 0) {
                throw new UnrecognizedOptionException(
                        "Extra arguments were provided in "
                                + Arrays.asList(args));
            }
        } catch (final ParseException e) {
            printHelp(options,
                    "Could not parse command line: " + Arrays.asList(args));
            return;
        }

        if (cmd.hasOption(OPTION_HELP)) {
            printHelp(options, null);
            return;
        }

        /**
         * Pick up the specified Port number
         */
        final int defaultPort = 8080;
        int port;
        if (cmd.hasOption(OPTION_PORT)) {
            final String val = cmd.getOptionValue(OPTION_PORT);
            try {
                port = Integer.parseInt(val);
            } catch (final NumberFormatException e) {
                printHelp(options, "Unexpected port " + val);
                return;
            }
        } else {
            port = defaultPort;
        }

        /**
         * Pick up the specified HTTP server
         */
        final String defaultHttpServer = "http://127.0.0.1:21029";
        final String httpServer;
        if (cmd.hasOption(OPTION_HTTP)) {
            httpServer = cmd.getOptionValue(OPTION_HTTP);
        } else {
            httpServer = defaultHttpServer;
        }

        /**
         * Pick up the specified file location
         */
        final String defaultFilePath = "target";
        final String filePath;
        if (cmd.hasOption(OPTION_FILE)) {
            filePath = cmd.getOptionValue(OPTION_FILE);
        } else {
            filePath = defaultFilePath;
        }

        /**
         * Initialize the database
         */
        final String defaultDBPath = "mongodb://127.0.0.1:27017";
        String dbPath;
        if (cmd.hasOption(OPTION_DB)) {
            dbPath = cmd.getOptionValue(OPTION_DB);
        } else {
            dbPath = defaultDBPath;
        }
        MongoClientURI dbURI = new MongoClientURI(dbPath);
        MongoClient dbClient = new MongoClient(dbURI);
        MongoDatabase db = dbClient.getDatabase("CSP");
        final PageJsonColl pageJsonColl = new PageJsonColl(db);

        /**
         * Create instance for the proxy with specific parameters
         */
        System.out.println("About to start server on port: " + port);
        HttpProxyServerBootstrap bootstrap = DefaultHttpProxyServer
                .bootstrapFromFile("./littleproxy.properties")
                .withPort(port)
                .withAllowLocalOnly(false)
                .withFiltersSource(new HttpFiltersSourceAdapter() {
                    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                        return new HttpFiltersAdapter(originalRequest) {
                            @Override
                            public HttpResponse requestPre(HttpObject httpObject) {
                                if (httpObject instanceof HttpRequest) {
                                    HttpRequest httpRequest = (HttpRequest) httpObject;
                                    if (httpRequest.headers().contains("Accept-Encoding")) {
                                        httpRequest.headers().remove("Accept-Encoding");
                                    }
                                }
                                return null;
                            }

                            @Override
                            public HttpResponse requestPost(HttpObject httpObject) {
                                return null;
                            }

                            @Override
                            public HttpObject responsePre(HttpObject httpObject) {
                                String url = originalRequest.getUri();
                                System.out.println("--------------Seprator--------------\n");
                                System.out.println("Response post -> " + url + " - " +
                                        httpObject.getClass() + " - " + httpObject);
                                if (httpObject instanceof FullHttpResponse) {
                                    String ctype = ((FullHttpResponse) httpObject).headers().get("Content-type");
                                    if (ctype != null) {
                                        System.out.println("Response text/html -> " + ctype);
                                        if (!ctype.startsWith("text/html")) {
                                            System.out.println("Not HTML, return!!!!");
                                            return httpObject;
                                        }
                                    } else {
                                        System.out.println("Not HTML, return!!!!");
                                        return httpObject;
                                    }
                                }

                                if (httpObject instanceof HttpContent) {
                                    String response = ((HttpContent) httpObject).content().toString(CharsetUtil.UTF_8);
                                    ByteBuf newHTML = buffer(1024);

                                    try {
                                        CSPApplier csp = new CSPApplier(response, url, filePath, httpServer,
                                                                        true, pageJsonColl);
                                        csp.analyzeJson();

                                        csp.generateJS();
                                        csp.generateCSS();
                                        csp.generateReport();

                                        String newDoc = csp.generateHTML();
                                        newHTML.writeBytes(newDoc.getBytes());
                                        ((HttpContent) httpObject).content().clear().writeBytes(newHTML);
                                        ((FullHttpResponse) httpObject).headers().set("Content-Length", newDoc.length());
                                        ((FullHttpResponse) httpObject).headers().set("Content-Security-Policy",
                                                csp.generateCSP());
                                    }
                                    catch(IOException e) {
                                        System.out.println("Cannot write new files to the specified location!\n");
                                        e.printStackTrace();
                                        return httpObject;
                                    }
                                    catch(NoSuchAlgorithmException e) {
                                        System.out.println("SHA1 encryption is not supported in the machine!\n");
                                        e.printStackTrace();
                                        return httpObject;
                                    }

                                }
                                return httpObject;
                            }

                            @Override
                            public HttpObject responsePost(HttpObject httpObject) {
                                // TODO: implement your filtering here
                                return httpObject;
                            }

                        };
                    }

                    @Override
                    public int getMaximumRequestBufferSizeInBytes() {
                        return Integer.MAX_VALUE;
                    }

                    @Override
                    public int getMaximumResponseBufferSizeInBytes() {
                        return Integer.MAX_VALUE;
                    }
                });

        if (cmd.hasOption(OPTION_MITM)) {
            LOG.info("Running as Man in the Middle");
            bootstrap.withManInTheMiddle(new SelfSignedMitmManager());
        }

        if (cmd.hasOption(OPTION_DNSSEC)) {
            final String val = cmd.getOptionValue(OPTION_DNSSEC);
            if (ProxyUtils.isTrue(val)) {
                LOG.info("Using DNSSEC");
                bootstrap.withUseDnsSec(true);
            } else if (ProxyUtils.isFalse(val)) {
                LOG.info("Not using DNSSEC");
                bootstrap.withUseDnsSec(false);
            } else {
                printHelp(options, "Unexpected value for " + OPTION_DNSSEC
                        + "=:" + val);
                return;
            }
        }

        System.out.println("About to start...");
        bootstrap.start();
    }

    private static void printHelp(final Options options,
                                  final String errorMessage) {
        if (!StringUtils.isBlank(errorMessage)) {
            LOG.error(errorMessage);
            System.err.println(errorMessage);
        }

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("littleproxy", options);
    }
}
