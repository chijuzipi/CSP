package org.cspapplier;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class CSPGeneratorTest {

    CSPGenerator CSPGen;
    URLContentAnalyzer urlContent;

    @Before
    public void setUp() throws Exception {
        String filePath = "src/test/resources/";
        String fileName = filePath + "index.html";
        String url = "www.test.com";
        String httpPath = "http://127.0.0.1:21029";

        File html = new File(fileName);
        Document doc = Jsoup.parse(html, "UTF-8");

        this.urlContent = new URLContentAnalyzer(doc.toString(), url);
        this.urlContent.generateJSElements();
        this.urlContent.generateCSSElements();

        this.CSPGen = new CSPGenerator(this.urlContent, httpPath);
    }

    @Test
    public void testGetCSPHeader() {
        this.CSPGen.generateCSPHeader();
        assertNotEquals("Content-Security-Policy: default-src 'self'; script-src 'self' ",
                this.CSPGen.getCSPHeader());
    }
}