package org.cspapplier;

import org.cspapplier.util.ElementEventBinder;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class URLContentAnalyzerTest {
    private URLContentAnalyzer getURL;

    @Before
    public void initialize() throws IOException {
        String fileName = "demo/index.html";
        this.getURL = new URLContentAnalyzer(fileName);
    }

    @Test
    public void testGenerateInlineElementEvents() {
        this.getURL.generateInlineElementEvents();

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

}