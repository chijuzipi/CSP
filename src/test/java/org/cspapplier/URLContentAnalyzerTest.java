package org.cspapplier;

import org.cspapplier.util.ElementEventBinder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class URLContentAnalyzerTest {
    private URLContentAnalyzer getURL;

    @Before
    public void initialize() throws IOException, NoSuchAlgorithmException {
        String fileName = "src/test/resources/index.html";
        String url = "www.test.com";

        File html = new File(fileName);
        Document doc = Jsoup.parse(html, "UTF-8");

        this.getURL = new URLContentAnalyzer(doc.toString(), url);
    }

    @Test
    public void testGenerateInlineJSElementEvents() {
        this.getURL.generateInlineJSElementEvents();

        Element element;
        String event;
        for (ElementEventBinder elementEvent : getURL.getInlineJSElementEvents()) {
            element = elementEvent.getElement();
            event = elementEvent.getEvent();

            if (event.equals("onclick") && element.attr("onclick").equals("myfunction1()")) {
                assertEquals("button1", element.id());
            }

            if (event.equals("onclick") && element.attr("onclick").equals("myfunction2()")) {
                assertEquals("button2", element.className());
            }

            if (event.equals("onmouseover") && element.attr("onmouseover").equals("myfunction4()")) {
                assertEquals("button2", element.className());
            }

            if (event.equals("onmouseover") && element.attr("onmouseover").equals("myfunction3()")) {
                assertEquals("button3", element.id());
            }

            if (event.equals("onclick") && element.attr("onclick").equals("myfunction3()")) {
                assertEquals("button3", element.id());
            }

            if (event.equals("onsubmit") && element.attr("onsubmit").equals("myfunction4()")) {
                assertEquals("get", element.attr("method"));
            }

            if (event.equals("onsubmit") && element.attr("onsubmit").equals("myfunction5()")) {
                assertEquals("form1", element.id());
            }
        }
    }

    @Test
    public void testGenerateCSSElements() throws Exception {
        getURL.generateCSSElements();
        assertEquals(1, getURL.getBlockCSSElements().size());
        assertEquals(3, getURL.getInlineCSSElements().size());
    }

}