package org.cspapplier;

/**
 * CSPGenerator
 *
 * Generate CSP header according to the results from URL analyzer.
 *
 */

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CSPGenerator {
	private String CSPHeader;
	private URLContentAnalyzer URLContent;

    public CSPGenerator(URLContentAnalyzer getURL)
    {
    	this.URLContent = getURL;
        this.CSPHeader = "Content-Security-Policy: default-src 'self'; script-src 'self' ";
    }

    public void generateCSPHeader(){
        for (Element y : URLContent.getExternalJSElements())
        {
            CSPHeader = CSPHeader + y.attr("src") + " ";
        }

        CSPHeader = CSPHeader + URLContent.getHashURL() + ".js ";
        // delete the last space
        CSPHeader = CSPHeader.substring(0, CSPHeader.length() - 1);
        CSPHeader = CSPHeader + "; ";
        CSPHeader = CSPHeader + "style-src 'self' ";
        for (Element x : URLContent.getExternalCSSElements())
        {
            CSPHeader = CSPHeader + x.attr("href") + " ";
        }
        CSPHeader = CSPHeader.substring(0, CSPHeader.length() - 1);
        CSPHeader = CSPHeader + "; ";
    }

    public void write() throws IOException {
        String fileName = this.URLContent.getOutputPath() + this.URLContent.getHashURL() + ".csp";
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
        OutputStreamWriter outputStream = new OutputStreamWriter(fileOutputStream);

        try {
            outputStream.append(this.CSPHeader);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
            fileOutputStream.close();
        }
    }
}
