package org.cspapplier.json;

import org.cspapplier.util.ElementEventBinder;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Shuangping on 8/1/2014.
 */

public class JSinJson {
    private String identity;
    private ArrayList<ElementInJson> elements;

    public JSinJson(String identity, Elements elements) {
        this.identity = identity;

        ElementInJson elementInJson;
        for (Element element : elements) {
            elementInJson = new ElementInJson(element);
            this.elements.add(elementInJson);
        }
    }

    public JSinJson(String identity, ArrayList<ElementEventBinder> elementEventBinders) {
        this.identity = identity;

        ElementInJson elementInJson;
        for (ElementEventBinder elementEvent : elementEventBinders) {
            elementInJson = new ElementInJson(elementEvent);
            this.elements.add(elementInJson);
        }
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public ArrayList<ElementInJson> getElements() {
        return elements;
    }

    public void setElements(ArrayList<ElementInJson> elements) {
        this.elements = elements;
    }
}
