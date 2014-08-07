package org.cspapplier;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * HTMLGenerator
 *
 * Generate new HTML file according to the analysis of JS and CSS of the original
 * web page.
 *
 */

public class HTMLGenerator {
    private final Document inputDOM;
    private final String newFileName;

    private BufferedWriter bufferJS;
    private BufferedWriter bufferCSS;
    private BufferedWriter bufferHTML;

    public HTMLGenerator(URLContentAnalyzer getURL) throws IOException {
        this.inputDOM = getURL.inputDOM;
        this.newFileName = getURL.newFileName;

        // Initialize counters to generate id for inline JS/CSS
        jsCount = 0;
        cssCount = 0;

        // Create a filename to write new HTML
        createNewHTMlFileName(input);

        File outputHtml = new File(newFileName + ".html");
        File outputScript = new File(newFileName + ".js");
        File outputCSS = new File(newFileName + ".css");

        // Prepare buffer for file IO
        bufferJS = new BufferedWriter(new FileWriter(outputScript));
        bufferCSS = new BufferedWriter(new FileWriter(outputCSS));
        bufferHTML = new BufferedWriter(new FileWriter(outputHtml));
    }

    public void createNewHTMlFileName(String originalFileName) throws IOException {
        String[] pathList = originalFileName.trim().split("/");
        String fileName = pathList[pathList.length - 1];

        String[] fileNameList = fileName.split("\\.");

        newFileName = fileNameList[fileNameList.length - 2];
        System.out.println("New HTML filename is " + newFileName);
    }

    public void generateHTML() throws IOException {
        // Add tags in HTMl to include external generated JS/CS
        addExternalJSToHtml();
        addExternalCSSToHtml();

        // Write html into a new file
        bufferHTML.write(inputDOM.toString());
        bufferHTML.close();
    }

    public void writeInlineJS(Elements inlineJSElements) throws IOException {
        // Write the document ready part
        bufferJS.write("\r\n");
        bufferJS.write("document.addEventListener('DOMContentReady', function () {");
        bufferJS.write("\r\n");

        for (Element jsElement : inlineJSElements) {
            extractEachInlineJS(jsElement);
        }

        bufferJS.write("});");
    }

    public void writeExternalJS(Elements externalJSElements) {
    }

    public void writeBlockJS(Elements blockJSElements) {
    }

    public void addExternalJSToHtml() {
        inputDOM.head().appendElement("script").attr("src", newFileName + ".js");
    }

    public void addExternalCSSToHtml() {
        inputDOM.head().appendElement("link").attr("rel", "stylesheet")
                .attr("type", "text/css").attr("href", newFileName + ".css");
    }
}
