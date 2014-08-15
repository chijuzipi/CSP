package org.cspapplier.json;

import org.cspapplier.HashMapGenerator;
import org.cspapplier.util.ElementEventBinder;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * HashMapInJson.java
 *
 * Contains HashMaps for js/css/inline scripts.
 * - key: ID hashed from the JS / CSS content.
 * - value: ArrayList of ElementInJson (Simplified description of the elements).
 * - Can be directly convert to json string.
 *
 */

public class HashMapInJson {
    private HashMap<String, ArrayList<ElementInJson>> js;
    private HashMap<String, ArrayList<ElementInJson>> css;

    public HashMapInJson() {
        this.js = new HashMap<String, ArrayList<ElementInJson>>();
        this.css = new HashMap<String, ArrayList<ElementInJson>>();
    }

    public void convertJS(HashMapGenerator hashMapGenerator) {

        for (String identity : hashMapGenerator.getExternalJSMap().keySet()) {
            this.js.put(identity, new ArrayList<ElementInJson>());
            for (Element element : hashMapGenerator.getExternalJSMap().get(identity)) {
                this.js.get(identity).add(new ElementInJson(element));
            }
        }

        for (String identity : hashMapGenerator.getBlockJSMap().keySet()) {
            this.js.put(identity, new ArrayList<ElementInJson>());
            for (Element element : hashMapGenerator.getBlockJSMap().get(identity)) {
                this.js.get(identity).add(new ElementInJson(element));
            }
        }

        for (String identity : hashMapGenerator.getInlineJSMap().keySet()) {
            this.js.put(identity, new ArrayList<ElementInJson>());
            for (ElementEventBinder elementEvent : hashMapGenerator.getInlineJSMap().get(identity)) {
                this.js.get(identity).add(new ElementInJson(elementEvent));
            }
        }
    }

    public void convertCSS(HashMapGenerator hashMapGenerator) {
        for (String identity : hashMapGenerator.getBlockCSSMap().keySet()) {
            this.css.put(identity, new ArrayList<ElementInJson>());
            for (Element element : hashMapGenerator.getBlockCSSMap().get(identity)) {
                this.css.get(identity).add(new ElementInJson(element));
            }
        }

        for (String identity : hashMapGenerator.getInlineCSSMap().keySet()) {
            this.css.put(identity, new ArrayList<ElementInJson>());
            for (Element element : hashMapGenerator.getInlineCSSMap().get(identity)) {
                this.css.get(identity).add(new ElementInJson(element));
            }
        }
    }

    public HashMap<String, ArrayList<ElementInJson>> getJs() {
        return this.js;
    }

    public HashMap<String, ArrayList<ElementInJson>> getCss() {
        return this.css;
    }

}
