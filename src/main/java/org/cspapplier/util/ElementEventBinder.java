package main.java.org.cspapplier.util;

import org.jsoup.nodes.Element;

/**
 * Created by Shuangping on 8/2/2014.
 */

public class ElementEventBinder {
    private Element element;
    private String event;

    public ElementEventBinder(Element inputElement, String inputEvent) {
        element = inputElement;
        event = inputEvent;
    }

    public Element getElement() {
        return element;
    }

    public String getEvent() {
        return event;
    }
}
