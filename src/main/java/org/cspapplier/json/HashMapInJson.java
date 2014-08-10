package org.cspapplier.json;

import org.cspapplier.util.ElementEventBinder;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * HashMapInJson.java
 *
 * Contains HashMaps for external/block/inline scripts.
 * - key: ID hashed from the JS / CSS content.
 * - value: ArrayList of ElementInJson (Simplified description of the elements).
 * - Can be directly convert to json string.
 *
 */

public class HashMapInJson {
    private HashMap<String, ArrayList<ElementInJson>> external;
    private HashMap<String, ArrayList<ElementInJson>> block;
    private HashMap<String, ArrayList<ElementInJson>> inline;

    public HashMapInJson() {
        this.external = new HashMap<String, ArrayList<ElementInJson>>();
        this.block = new HashMap<String, ArrayList<ElementInJson>>();
        this.inline = new HashMap<String, ArrayList<ElementInJson>>();
    }

    public HashMapInJson(HashMap<String, Elements> externalJS,
                         HashMap<String, Elements> blockJS,
                         HashMap<String, ArrayList<ElementEventBinder>> inlineJS) {
        this.external = new HashMap<String, ArrayList<ElementInJson>>();
        this.block = new HashMap<String, ArrayList<ElementInJson>>();
        this.inline = new HashMap<String, ArrayList<ElementInJson>>();

        ElementInJson elementInJson;

        ArrayList<ElementInJson> externalElements;
        for (String identity : externalJS.keySet()) {
            externalElements = new ArrayList<ElementInJson>();
            for (Element element : externalJS.get(identity)) {
                elementInJson = new ElementInJson(element);
                externalElements.add(elementInJson);
            }
            this.external.put(identity, externalElements);
        }

        ArrayList<ElementInJson> blockElements;
        for (String identity : blockJS.keySet()) {
            blockElements = new ArrayList<ElementInJson>();
            for (Element element : blockJS.get(identity)) {
                elementInJson = new ElementInJson(element);
                blockElements.add(elementInJson);
            }
            this.block.put(identity, blockElements);
        }

        ArrayList<ElementInJson> inlineElements;
        for (String identity : inlineJS.keySet()) {
            inlineElements = new ArrayList<ElementInJson>();
            for (ElementEventBinder elementEvent : inlineJS.get(identity)) {
                elementInJson = new ElementInJson(elementEvent);
                inlineElements.add(elementInJson);
            }
            this.inline.put(identity, inlineElements);
        }
    }

    public HashMap<String, ArrayList<ElementInJson>> getExternal() {
        return this.external;
    }

    public HashMap<String, ArrayList<ElementInJson>> getBlock() {
        return this.block;
    }

    public HashMap<String, ArrayList<ElementInJson>> getInline() {
        return this.inline;
    }
}
