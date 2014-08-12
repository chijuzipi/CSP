package main.java.org.cspapplier;

import main.java.org.cspapplier.util.ElementEventBinder;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLContentAnalyzer
{
    private Document inputDOM;
    private Elements externalJSElements;
    private Elements blockJSElements;
    private ArrayList<ElementEventBinder> inlineJSElementEvents;

    public URLContentAnalyzer(String input) throws IOException {
        inlineJSElementEvents = new ArrayList<ElementEventBinder>();

        // Parse the DOM structure of the input URL content
        File inputFile = new File(input);
        this.inputDOM = Jsoup.parse(inputFile, "UTF-8");
    }

    public void generateJSElements() {
        generateExternalElements();
        generateBlockElements();
        generateInlineElementEvents();
    }

    public void generateExternalElements() {
        this.externalJSElements = inputDOM.select("script[src]");
    }

    public void generateBlockElements() {
        this.blockJSElements = inputDOM.select("script").not("script[src]");
    }

    public void generateInlineElementEvents() {
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

        Elements inlineElements;
        ElementEventBinder elementEvent;
        for (String event : events) {
            inlineElements = inputDOM.select("[" + event + "]");
            if (inlineElements.size() > 0) {
                for (Element element : inlineElements) {
                    elementEvent = new ElementEventBinder(element, event);
                    this.inlineJSElementEvents.add(elementEvent);
                }
            }
        }
    }

//    public void extractCSS() throws IOException {
//        // Select and write block style to .css file and remove it from HTML
//        extractBlockCSS();
//
//        // Select and write inline style to .css file and remove it from HTML
//        extractInlineCSS();
//    }

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

    public Document getInputDOM() {
        return inputDOM;
    }

    public Elements getExternalJSElements() {
        return externalJSElements;
    }

    public void setExternalJSElements(Elements externalJSElements) {
        this.externalJSElements = externalJSElements;
    }

    public Elements getBlockJSElements() {
        return blockJSElements;
    }

    public void setBlockJSElements(Elements blockJSElements) {
        this.blockJSElements = blockJSElements;
    }
    public ArrayList<ElementEventBinder> getInlineJSElementEvents() {
        return inlineJSElementEvents;
    }

    public void setInlineJSElementEvents(ArrayList<ElementEventBinder> inlineJSElementEvents) {
        this.inlineJSElementEvents = inlineJSElementEvents;
    }
}
