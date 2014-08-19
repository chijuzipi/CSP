package org.cspapplier.json;

import org.cspapplier.util.ElementEventBinder;
import org.jsoup.nodes.Element;

/**
 * ElementInJson.java
 *
 * A simplified description of the HTML element.
 */

public class ElementInJson {
    private String tag;
    private String id;
    private String className;
    private String src;
    private String event;
    private String xpath;

    public ElementInJson(Element element) {
        this.tag = element.tagName();
        this.id = element.id();
        this.className = element.className();
        this.src = element.attr("src");
        this.event = "";
        this.xpath = xpath(element);
    }

    public ElementInJson(ElementEventBinder elementEvent) {
        Element element = elementEvent.getElement();
        String event = elementEvent.getEvent();

        this.tag = element.tagName();
        this.id = element.id();
        this.className = element.className();
        this.src = "";
        this.event = event;
        this.xpath = getXPath(element);
    }

    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 1;

        result = prime * result + ((this.tag == null) ? 0 : this.tag.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.className == null) ? 0 : this.className.hashCode());
        result = prime * result + ((this.src == null) ? 0 : this.src.hashCode());
        result = prime * result + ((this.event == null) ? 0 : this.event.hashCode());
        result = prime * result + ((this.xpath == null) ? 0 : this.xpath.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;

        if (object != null && object instanceof ElementInJson) {
            isEqual = (this.tag.equals(((ElementInJson) object).tag)) &&
                      (this.id.equals(((ElementInJson) object).id)) &&
                      (this.className.equals(((ElementInJson) object).className)) &&
                      (this.src.equals(((ElementInJson) object).src)) &&
                      (this.event.equals(((ElementInJson) object).event)) &&
                      (this.xpath.equals(((ElementInJson) object).xpath));

        }
        return isEqual;
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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getXpath(Element x) {
    	Element parent = x.parent();
    	if (parent == null){
    		return "//Document/";
    	}
        return getXpath(parent) + x.tag()+ "[" + x.elementSiblingIndex() + "]" + "/";
    }
    
    public String xpath(Element x){
    	String result = getXpath(x);
    	return result;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }
}
