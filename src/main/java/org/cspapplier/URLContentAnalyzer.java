package org.cspapplier;

import org.cspapplier.json.JsonGenerator;
import org.cspapplier.util.ElementEventBinder;
import org.cspapplier.util.SHAHash;

import java.util.ArrayList;
import java.util.HashMap;

import java.io.File;
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
    public Document inputDOM;

    public HashMap<String, Elements> externalJSMap;
    public HashMap<String, Elements> blockJSMap;
    public HashMap<String, ArrayList<ElementEventBinder>> inlineJSMap;

    public URLContentAnalyzer(String input) throws IOException {
        externalJSMap = new HashMap<String, Elements>();
        blockJSMap = new HashMap<String, Elements>();
        inlineJSMap = new HashMap<String, ArrayList<ElementEventBinder>>();

        File inputFile = new File(input);

        // Parse the DOM structure of the input URL content
        this.inputDOM = Jsoup.parse(inputFile, "UTF-8");

        generateJSHashMap();
    }

    public void generateJSHashMap() throws IOException {
        generateExternalHashMap();
        generateBlockHashMap();
        generateInlineHashMap();
    }

//    public void extractCSS() throws IOException {
//        // Select and write block style to .css file and remove it from HTML
//        extractBlockCSS();
//
//        // Select and write inline style to .css file and remove it from HTML
//        extractInlineCSS();
//    }

    public void generateExternalHashMap() throws Exception {
        Elements externalJSElements = inputDOM.select("script[src]");
        Elements storeElements;
        String identity;
        for (Element element : externalJSElements) {
            identity = SHAHash.getHashCode(element.attr("src"));
            storeElements = externalJSMap.get(identity);
            if (storeElements == null) {
                storeElements = new Elements();
                storeElements.add(element);
            } else {
                if (!storeElements.contains(element)) {
                    storeElements.add(element);
                }
            }
        }
    }
    public void generateBlockHashMap() throws Exception {
        Elements blockJSElements = inputDOM.select("script").not("script[src]");
        Elements storeElements;
        String identity;
        for (Element element : blockJSElements) {
            identity = SHAHash.getHashCode(element.attr("src"));
            storeElements = externalJSMap.get(identity);
            if (storeElements == null) {
                storeElements = new Elements();
                storeElements.add(element);
            } else {
                if (!storeElements.contains(element)) {
                    storeElements.add(element);
                }
            }
        }
    }

    public void generateInlineHashMap() throws IOException {
        String[] events = {
            // Mouse Events
            "onclick", "ondblclick", "onmousedown", "onmousemove", "onmouseover", "onmouseout", "onmouseup",
            // Keyboard Events
            "onkeydown", "onkeypress", "onkeyup",
            // Frame/Object Events
            "onabort", "onerror", "onload", "onresize", "onscroll", "onunload",
            // Form Events
            "onblur", "onchange", "onfocus", "onreset", "onselect", "onsubmit"
        };

        ArrayList<ElementEventBinder> storeElements;
        for (String event : events) {
            inputDOM.select("[" + event + "]");
        }
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

//    public void extractBlockCSS() throws IOException {
//        Elements inlineCSS = inputDOM.select("style");
//        for (Element cssElement : inlineCSS){
//            bufferCSS.write(cssElement.data());
//            // Delete the element
//            cssElement.remove();
//        }
//    }
//    public void extractInlineCSS() throws IOException {
//        Elements inlineCSS = inputDOM.select("[style]");
//        for (Element elementCSS : inlineCSS) {
//            String cssID;
//            if (elementCSS.id().equals("")) {
//                elementCSS.attr("id", String.valueOf(cssCount));
//                cssID = String.valueOf(cssCount);
//                cssCount++;
//            } else {
//                cssID = elementCSS.id();
//            }
//            String cssContent = elementCSS.attr("style");
//            elementCSS.removeAttr("style");
//
//            bufferCSS.write("\r\n");
//            bufferCSS.write("#" + cssID + " {" + cssContent + "}");
//            bufferCSS.write("\r\n");
//        }
//    }

    public void generateJSJSON(JsonGenerator jsJson) {
        gson
    }

    public static void main(String[] args) throws IOException {
        String htmlFile = "demo/Twitter.html";

        URLContentAnalyzer getURL = new URLContentAnalyzer(htmlFile);
        getURL.extractJS();

        //HTMLGenerator newHTML = new HTMLGenerator(getURL);
        //newHTML.generateHTML();
    }
}
