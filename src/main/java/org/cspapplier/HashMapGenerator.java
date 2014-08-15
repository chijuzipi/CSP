package org.cspapplier;

import org.cspapplier.util.ElementEventBinder;
import org.cspapplier.util.SHAHash;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * HashMapGenerator.java
 *
 * Generate HashMaps from the HTMl elements
 * - Keys: SHA1 hash from JS / CSS content
 * - Values: The elements containing the JS / CSS
 *
 */

public class HashMapGenerator {

    private HashMap<String, Elements> externalJSMap;
    private HashMap<String, Elements> blockJSMap;
    private HashMap<String, ArrayList<ElementEventBinder>> inlineJSMap;

    private HashMap<String, Elements> blockCSSMap;
    private HashMap<String, Elements> inlineCSSMap;

    public HashMapGenerator() {
        this.externalJSMap = new HashMap<String, Elements>();
        this.blockJSMap = new HashMap<String, Elements>();
        this.inlineJSMap = new HashMap<String, ArrayList<ElementEventBinder>>();

        this.blockCSSMap = new HashMap<String, Elements>();
        this.inlineCSSMap = new HashMap<String, Elements>();
    }

    public void generateJSElementHashMap(URLContentAnalyzer urlContentAnalyzer) throws NoSuchAlgorithmException {
        generateExternalJSHashMap(urlContentAnalyzer.getExternalJSElements());
        generateBlockJSHashMap(urlContentAnalyzer.getBlockJSElements());
        generateInlineJSHashMap(urlContentAnalyzer.getInlineJSElementEvents());
    }

    public void generateExternalJSHashMap(Elements externalJSElements) throws NoSuchAlgorithmException {
        String identity;
        for (Element element : externalJSElements) {
            identity = SHAHash.getHashCode("externalJS" + element.attr("src"));
            if (externalJSMap.get(identity) == null) {
                externalJSMap.put(identity, new Elements());
            }
            externalJSMap.get(identity).add(element);
        }
    }

    public void generateBlockJSHashMap(Elements blockJSElements) throws NoSuchAlgorithmException {
        String identity;
        for (Element element : blockJSElements) {
            identity = SHAHash.getHashCode("blockJS" + element.data());
            if (blockJSMap.get(identity) == null) {
                blockJSMap.put(identity, new Elements());
            }
            blockJSMap.get(identity).add(element);
        }
    }

    public void generateInlineJSHashMap(ArrayList<ElementEventBinder> elementEvents) throws NoSuchAlgorithmException {
        Element element;
        String event;
        String identity;
        if (elementEvents.size() > 0) {
            for (ElementEventBinder elementEvent : elementEvents) {
                element = elementEvent.getElement();
                event = elementEvent.getEvent();
                identity = SHAHash.getHashCode("inlineJS" + element.attr(event));
                if (inlineJSMap.get(identity) == null) {
                    inlineJSMap.put(identity, new ArrayList<ElementEventBinder>());
                }
                inlineJSMap.get(identity).add(elementEvent);
            }
        }
    }

    public void generateCSSElementHashmap(URLContentAnalyzer urlContentAnalyzer) throws NoSuchAlgorithmException {
        String identity;
        for (Element element : urlContentAnalyzer.getBlockCSSElements()) {
            identity = SHAHash.getHashCode("blockCSS" + element.data());
            if (this.blockCSSMap.get(identity) == null) {
                this.blockCSSMap.put(identity, new Elements());
            }
            this.blockCSSMap.get(identity).add(element);
        }

        for (Element element : urlContentAnalyzer.getInlineCSSElements()) {
            identity = SHAHash.getHashCode("inlineCSS" + element.attr("style"));
            if (this.inlineCSSMap.get(identity) == null) {
                this.inlineCSSMap.put(identity, new Elements());
            }
            this.inlineCSSMap.get(identity).add(element);
        }
    }

    public HashMap<String, Elements> getExternalJSMap() {
        return externalJSMap;
    }

    public void setExternalJSMap(HashMap<String, Elements> externalJSMap) {
        this.externalJSMap = externalJSMap;
    }

    public HashMap<String, Elements> getBlockJSMap() {
        return blockJSMap;
    }

    public void setBlockJSMap(HashMap<String, Elements> blockJSMap) {
        this.blockJSMap = blockJSMap;
    }

    public HashMap<String, ArrayList<ElementEventBinder>> getInlineJSMap() {
        return inlineJSMap;
    }

    public void setInlineJSMap(HashMap<String, ArrayList<ElementEventBinder>> inlineJSMap) {
        this.inlineJSMap = inlineJSMap;
    }


    public HashMap<String, Elements> getBlockCSSMap() {
        return blockCSSMap;
    }

    public void setBlockCSSMap(HashMap<String, Elements> blockCSSMap) {
        this.blockCSSMap = blockCSSMap;
    }

    public HashMap<String, Elements> getInlineCSSMap() {
        return inlineCSSMap;
    }

    public void setInlineCSSMap(HashMap<String, Elements> inlineCSSMap) {
        this.inlineCSSMap = inlineCSSMap;
    }
}
