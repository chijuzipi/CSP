package org.cspapplier;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * URLContentAnalyzer
 *
 * Analyze the web content pulled from given URL. Inline & block Javascript
 * and CSS are extracted to external files.
 */

public class URLContentAnalyzer
{
	// For creating id for extracted elements
	private int jsCount;
	private int cssCount;

	private BufferedWriter bufferJS;
	private BufferedWriter bufferCSS;

	public Document inputDOM;
	public String newFileName;

	public URLContentAnalyzer(String input) throws IOException {
		// Initialize counters to generate id for inline JS/CSS
		jsCount = 0;
		cssCount = 0;

        // Create a filename to write new HTML
        createNewHTMlFileName(input);

        // Prepare IO files
        File inputFile = new File(input);
        File outputScript = new File(newFileName + ".js");
        File outputCSS = new File(newFileName + ".css");

        // Prepare buffer for file IO
		bufferJS = new BufferedWriter(new FileWriter(outputScript));
		bufferCSS = new BufferedWriter(new FileWriter(outputCSS));

		// Parse the DOM structure of the input URL content
		inputDOM = Jsoup.parse(inputFile, "UTF-8");
	}

    public void createNewHTMlFileName(String originalFileName) throws IOException {
        String[] pathList = originalFileName.trim().split("/");
        String fileName = pathList[pathList.length - 1];
        System.out.println(fileName);

        String[] fileNameList = fileName.split("\\.");
        System.out.println(fileNameList.length);

        newFileName = fileNameList[fileNameList.length - 2];
        System.out.println(newFileName);
    }

    public void extractJS() throws IOException {
        // Select and write block script to .js file and remove it from HTML
        extractBlockJS();

        // Select and write inline script to .js file and remove it from HTML
        extractInlineJS();

        bufferJS.close();
    }

    public void extractCSS() throws IOException {
        // Select and write block style to .css file and remove it from HTML
        extractBlockCSS();

        // Select and write inline style to .css file and remove it from HTML
        extractInlineCSS();

        bufferCSS.close();
    }

    public void extractBlockJS() throws IOException {
        Elements blockJSElements = inputDOM.select("script").not("script[src]");
        for (Element jsElement : blockJSElements){
            bufferJS.write(jsElement.data());

            // Remove the element
            jsElement.remove();
        }
    }

    public void extractInlineJS() throws IOException {
        Elements inlineJSElements;
        String[] triggerEvents = {
            // Mouse Events
            "onclick", "ondblclick", "onmousedown", "onmousemove", "onmouseover", "onmouseout", "onmouseup",
            // Keyboard Events
            "onkeydown", "onkeypress", "onkeyup",
            // Frame/Object Events
            "onabort", "onerror", "onload", "onresize", "onscroll", "onunload",
            // Form Events
            "onblur", "onchange", "onfocus", "onreset", "onselect", "onsubmit"
        };

        // Write the document ready part
        bufferJS.write("\r\n");
        bufferJS.write("document.addEventListener('DOMContentReady', function () {");
        bufferJS.write("\r\n");

        for (String event : triggerEvents) {
            inlineJSElements = inputDOM.select("[" + event +"]");
            extractEachInlineJS(inlineJSElements, event);
        }

        bufferJS.write("});");
    }

	// Write inline script to external .js file
	public void extractEachInlineJS(Elements inlineJSElements, String event) throws IOException {
		// Remove first two characters (e.g. "on" in "onclick")
		String jsTriggerEvent = event.substring(2);
		for (Element jsElement : inlineJSElements)
		{
			// add id check, if exists then use it. otherwise create a new one
			String jsElementID = jsElement.id();
			if (jsElementID.equals(""))
			{
				// Convert to String
				jsElement.attr("id", String.valueOf(jsCount));
				jsElementID = String.valueOf(jsCount);
				jsCount++;
			}
			
			String inlineJSContent = jsElement.attr(event);
			
			// Delete trigger attribute
			jsElement.removeAttr(event);

            // Add event listener for the js
			bufferJS.write("\r\n");
			bufferJS.write("var element_" + jsElementID + " = document.getElementById(\"" + jsElementID + "\");");
			bufferJS.write("\r\n");
			bufferJS.write("element_" + jsElementID + ".addEventListener(\"" + jsTriggerEvent
                           + "\", function(){" + inlineJSContent + "}, false);" );
		}
	}

    public void extractBlockCSS() throws IOException {
        Elements inlineCSS = inputDOM.select("style");
        for (Element cssElement : inlineCSS){
            bufferCSS.write(cssElement.data());
            // Delete the element
            cssElement.remove();
        }
    }
    public void extractInlineCSS() throws IOException {
        Elements inlineCSS = inputDOM.select("[style]");
        for (Element elementCSS : inlineCSS) {
            String cssID;
            if (elementCSS.id().equals("")) {
                elementCSS.attr("id", String.valueOf(cssCount));
                cssID = String.valueOf(cssCount);
                cssCount++;
            } else {
                cssID = elementCSS.id();
            }
            String cssContent = elementCSS.attr("style");
            elementCSS.removeAttr("style");

            bufferCSS.write("\r\n");
            bufferCSS.write("#" + cssID + " {" + cssContent + "}");
            bufferCSS.write("\r\n");
        }
    }

	public static void main(String[] args) throws IOException {
		String htmlFile = "demo/Twitter.html";

		URLContentAnalyzer getURL = new URLContentAnalyzer(htmlFile);
		getURL.extractJS();

        HTMLGenerator newHTML = new HTMLGenerator(getURL);
        newHTML.generateHTML();
	}
}
