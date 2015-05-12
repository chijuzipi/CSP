package org.cspapplier;

import org.cspapplier.util.ElementEventBinder;

import org.cspapplier.util.SHAHash;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * URLContentGenerator
 *
 * Generate new HTML file with extra CSS/JS files from URLContentAnalyzer and HashMapGenerator
 *
 **/

public class URLContentGenerator {

    private URLContentAnalyzer urlContentAnalyzer;
    private HashMapGenerator hashMapGenerator;
    private String httpPath;
    private String filePath;

    // For generate random ID
    private Random randomGenerator;

    public URLContentGenerator(URLContentAnalyzer urlContentAnalyzer,
                               HashMapGenerator hashMapGenerator,
                               String httpPath, String filePath) {
        this.urlContentAnalyzer = urlContentAnalyzer;
        this.hashMapGenerator = hashMapGenerator;
        this.httpPath = generateCompletePath(httpPath);
        this.filePath = generateCompletePath(filePath);

        this.randomGenerator = new Random(System.currentTimeMillis());
    }

    /**
     * generateJS:
     *
     * Generate the JS file to the local directory
     *
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void generateJS() throws IOException, NoSuchAlgorithmException {
        String path = filePath + urlContentAnalyzer.getHashURL() + "/";
        String fileName = urlContentAnalyzer.getHashURL();

        File directory = new File(path);
        if (!directory.exists())
            directory.mkdirs();

        generateBlockJS(path, fileName);
        generateInlineJS(path, fileName + "_inline");

    }

    private void generateBlockJS(String path, String fileName) throws IOException {
        for (String jsID : hashMapGenerator.getBlockJSMap().keySet()) {
            File outputJS= new File(path + fileName + "_" + jsID + ".js");
            BufferedWriter bufferJS = new BufferedWriter(new FileWriter(outputJS));

            bufferJS.write("\r\n");
            bufferJS.write(hashMapGenerator.getBlockJSMap().get(jsID).get(0).data());
            bufferJS.write("\r\n");
            bufferJS.close();
        }
    }
    
    private void generateInlineJS(String path, String fileName) throws IOException {
        File outputJS= new File(path + fileName + ".js");
        BufferedWriter bufferJS = new BufferedWriter(new FileWriter(outputJS));

        // Write the document ready part
        bufferJS.write("\r\n");
        bufferJS.write("document.addEventListener('DOMContentLoaded', function () {");
        bufferJS.write("\r\n");

        Element element;
        String event;
        String elementID;
        for (String jsID : hashMapGenerator.getInlineJSMap().keySet()) {
            for (ElementEventBinder elementEvent : hashMapGenerator.getInlineJSMap().get(jsID)) {
                element = elementEvent.getElement();
                event = elementEvent.getEvent();

                elementID = element.id();
                if (elementID.equals("")) {
                    elementID = generateElementID();
                    element.attr("id", elementID);
                }

                String jsContent = element.attr(event);

                bufferJS.write("\r\n");
                bufferJS.write("var element_" + elementID +
                        " = document.getElementById(\"" + elementID + "\");");
                bufferJS.write("\r\n");
                bufferJS.write("element_" + elementID + ".addEventListener(\"" + event.substring(2) +
                        "\", function() {" + jsContent + "}, false);");
            }
        }
        bufferJS.write("});");
    }

    public void generateCSS() throws IOException, NoSuchAlgorithmException {
        String path = filePath + urlContentAnalyzer.getHashURL() + "/";
        String fileName = urlContentAnalyzer.getHashURL();

        File directory = new File(path);
        if (!directory.exists())
            directory.mkdirs();

        File outputCSS = new File(path + fileName + ".css");
        BufferedWriter bufferCSS = new BufferedWriter(new FileWriter(outputCSS));

        generateBlockCSS(bufferCSS);
        generateInlineCSS(bufferCSS);

        bufferCSS.close();
    }

    public void generateBlockCSS(BufferedWriter bufferCSS) throws IOException {
        for (String cssID : hashMapGenerator.getBlockCSSMap().keySet()) {
            bufferCSS.write(hashMapGenerator.getBlockCSSMap().get(cssID).get(0).data());
        }
    }

    public void generateInlineCSS(BufferedWriter bufferCSS) throws IOException, NoSuchAlgorithmException {
        String elementID;
        for (String cssID : hashMapGenerator.getInlineCSSMap().keySet()) {
            for (Element element : hashMapGenerator.getInlineCSSMap().get(cssID)) {
                elementID = element.id();
                if (elementID.equals("")) {
                    elementID = generateElementID();
                    element.attr("id", elementID);
                }

                String cssContent = element.attr("style");
                bufferCSS.write("\r\n");
                bufferCSS.write("#" + elementID + " {" + cssContent + "}");
                bufferCSS.write("\r\n");
            }
        }
    }

    public String generateHTML() {
        String path = httpPath + urlContentAnalyzer.getHashURL() + "/";
        String srcName = urlContentAnalyzer.getHashURL();
        Document doc = urlContentAnalyzer.getInputDOM();

        // Remove block JS
        for (Element element : urlContentAnalyzer.getBlockJSElements()){
            String identity = SHAHash.getHashCode("blockJS" + element.data());
            element.after("<script type=text/javascript src=" +
                    path + srcName + "_" + identity + ".js></script>");
            element.remove();
        }
        
        // Remove inline JS
        for (ElementEventBinder elementEvent : urlContentAnalyzer.getInlineJSElementEvents()){
            elementEvent.getElement().removeAttr(elementEvent.getEvent());
        }

        // Remove block CSS
        for (Element element : urlContentAnalyzer.getBlockCSSElements()){
            element.remove();
        }

        // Remove inline CSS
        for(Element element : urlContentAnalyzer.getInlineCSSElements()){
            element.removeAttr("style");
        }
        
        // Add external JS file to html
        doc.body().appendElement("script").attr("src", path + srcName + "_inline.js");
        
        // Add external CSS file to html
        doc.head().appendElement("link").attr("rel", "stylesheet")
                  .attr("type", "text/css").attr("href", path + srcName + ".css");

        /**
         * Output the HTML to string for proxy to handle
         */
        return doc.toString();
    }

    public String generateElementID() {
        double randomNumber = randomGenerator.nextDouble();
        return SHAHash.getHashCode(String.valueOf(randomNumber));
    }

    static String generateCompletePath(String path) {
        char lastChar = path.charAt(path.length() - 1);
        return path + ((lastChar != '/' && lastChar != '\\') ? "/" : "");
    }

    public String getHttpPath() {
        return this.httpPath;
    }

    public String getFilePath() {
        return this.filePath;
    }
}
