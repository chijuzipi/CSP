package org.cspapplier;

import org.cspapplier.util.ElementEventBinder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.cspapplier.util.SHAHash;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLContentAnalyzer
{
    private Document inputDOM;
    private String hashURL;
    private String URL;

    private Elements externalJSElements;
    private Elements blockJSElements;
    private ArrayList<ElementEventBinder> inlineJSElementEvents;

    private Elements blockCSSElements;
    private Elements inlineCSSElements;
    private Elements externalCSSElements;

    public URLContentAnalyzer(String content, String url) throws NoSuchAlgorithmException {
        inlineJSElementEvents = new ArrayList<ElementEventBinder>();

        // Parse the DOM structure of the input URL content
        this.hashURL  = SHAHash.getHashCode(splitURL(url));
        this.inputDOM = Jsoup.parse(content, "UTF-8");
        this.URL      = url;
    }

    public void generateJSElements() {
        generateExternalJSElements();
        generateBlockJSElements();
        generateInlineJSElementEvents();
    }

    public void generateExternalJSElements() {
        this.externalJSElements = inputDOM.select("script[src]");
    }

    public void generateBlockJSElements() {
        this.blockJSElements = inputDOM.select("script").not("script[src]");
    }

    public void generateInlineJSElementEvents() {
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

    public void generateCSSElements() {
    	generateExternalCSSElements();
        generateBlockCSSElements();
        generateInlineCSSElements();
    }
    
    public void generateExternalCSSElements(){
    	this.externalCSSElements = inputDOM.select("link[href$=.css]");
    }

    public void generateBlockCSSElements() {
        this.blockCSSElements = inputDOM.select("style");
    }

    public void generateInlineCSSElements() {
        this.inlineCSSElements = inputDOM.select("[style]");
    }

    /**
     * Split the input URL from "?" and reserve the first part
     * @param url
     *     The input URL
     * @return String
     */
    public String splitURL(String url) {
        return url.split("\\?")[0];
    }

    /**
     * Getters & Setters
     */
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

    public String getHashURL() {
        return hashURL;
    }

    public String getURL() {
        return URL;
    }

    public void setHashURL(String hashURL) {
        this.hashURL = hashURL;
    }
    
    public Elements getExternalCSSElements(){
    	return externalCSSElements;
    }
    
    public void setExternalCSSElements(Elements externalCSSElements){
    	this.externalCSSElements = externalCSSElements;
    }

    public Elements getBlockCSSElements() {
        return blockCSSElements;
    }

    public void setBlockCSSElements(Elements blockCSSElements) {
        this.blockCSSElements = blockCSSElements;
    }

    public Elements getInlineCSSElements() {
        return inlineCSSElements;
    }

    public void setInlineCSSElements(Elements inlineCSSElements) {
        this.inlineCSSElements = inlineCSSElements;
    }

}
