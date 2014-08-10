package org.cspapplier;

import org.cspapplier.util.ElementEventBinder;
import org.cspapplier.util.SHAHash;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shuangping on 8/10/2014.
 */

public class HashMapGenerator {

    private HashMap<String, Elements> externalJSMap;
    private HashMap<String, Elements> blockJSMap;
    private HashMap<String, ArrayList<ElementEventBinder>> inlineJSMap;

    public HashMapGenerator() {
        this.externalJSMap = new HashMap<String, Elements>();
        this.blockJSMap = new HashMap<String, Elements>();
        this.inlineJSMap = new HashMap<String, ArrayList<ElementEventBinder>>();
    }

    public void generateExternalHashMap(Elements externalJSElements) throws Exception {
        String identity;
        for (Element element : externalJSElements) {
            identity = SHAHash.getHashCode(element.attr("src"));
            if (externalJSMap.get(identity) == null) {
                externalJSMap.put(identity, new Elements());
            }
            externalJSMap.get(identity).add(element);
        }
    }

    public void generateBlockHashMap(Elements blockJSElements) throws Exception {
        String identity;
        for (Element element : blockJSElements) {
            identity = SHAHash.getHashCode(element.data());
            if (blockJSMap.get(identity) == null) {
                blockJSMap.put(identity, new Elements());
            }
            blockJSMap.get(identity).add(element);
        }
    }

    public void generateInlineHashMap(ArrayList<ElementEventBinder> elementEvents) throws Exception {
        Element element;
        String event;
        String identity;
        if (elementEvents.size() > 0) {
            for (ElementEventBinder elementEvent : elementEvents) {
                element = elementEvent.getElement();
                event = elementEvent.getEvent();
                identity = SHAHash.getHashCode(element.attr(event));
                if (inlineJSMap.get(identity) == null) {
                    inlineJSMap.put(identity, new ArrayList<ElementEventBinder>());
                }
                inlineJSMap.get(identity).add(elementEvent);
            }
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
}
