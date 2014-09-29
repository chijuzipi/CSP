package org.cspapplier.json;

import org.cspapplier.util.ElementEventBinder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ElementInJsonTest {

    private ElementInJson elementInJson1;
    private ElementInJson elementInJson2;
    private ElementInJson elementInJson3;

    String html = "<!DOCTYPE html><html><head></head><body>\n" +
                  "<p id=test class=testClass onclick=\"test()\">My first paragraph.</p>\n" +
                  "</body></html>";
    @Before
    public void createElement() {
        Document dom = Jsoup.parse(html);
        Element element = dom.select("p").get(0);
        ElementEventBinder elementEvent = new ElementEventBinder(element, "onclick");
        this.elementInJson1 = new ElementInJson(element);
        this.elementInJson2 = new ElementInJson(element);
        this.elementInJson3 = new ElementInJson(elementEvent);
    }

    @Test
    public void testHashCode() throws Exception {
        int hashCode1 = elementInJson1.hashCode();
        int hashCode2 = elementInJson2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(elementInJson1, elementInJson2);
    }

    @Test
    public void testAttributes() throws Exception {
        assertEquals("p", elementInJson1.getTag());
        assertEquals("test", elementInJson1.getId());
        assertEquals("testClass", elementInJson1.getClassName());
        assertEquals("", elementInJson1.getSrc());
        assertEquals("", elementInJson1.getEvent());
        assertEquals("//Document/html[0]/body[1]/p[0]/", elementInJson1.getXpath());
    }

    @Test
    public void testEventAttributes() throws Exception {
        assertEquals("p", elementInJson3.getTag());
        assertEquals("test", elementInJson3.getId());
        assertEquals("testClass", elementInJson3.getClassName());
        assertEquals("", elementInJson3.getSrc());
        assertEquals("onclick", elementInJson3.getEvent());
        assertEquals("//Document/html[0]/body[1]/p[0]/", elementInJson3.getXpath());
    }
}