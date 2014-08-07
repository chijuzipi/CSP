package org.cspapplier.json;

import org.cspapplier.util.ElementEventBinder;
import org.jsoup.nodes.Element;

/**
 * Created by Shuangping on 8/6/2014.
 */

public class ElementInJson {
    private String tag;
    private String id;
    private String className;
    private String event;
    private String xpath;

    public ElementInJson(Element element) {
        this.tag = element.tagName();
        this.id = element.id();
        this.className = element.className();
        this.event = "";
        this.xpath = getXPath(element);
    }

    public ElementInJson(ElementEventBinder elementEvent) {
        Element element = elementEvent.getElement();
        String event = elementEvent.getEvent();

        this.tag = element.tagName();
        this.id = element.id();
        this.className = element.className();
        this.event = event;
        this.xpath = getXPath(element);
    }

    // FIXME: Complete the getXPath function
    private String getXPath(Element element) {
        return "";
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }
}
