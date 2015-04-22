package org.cspapplier;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CSPGeneratorTest {

    CSPGenerator CSPGen;
    URLContentAnalyzer urlContent;

    @Before
    public void setUp() throws Exception {
        String filePath = "src/test/resources/";
        String fileName = filePath + "index.html";
        String url = "www.test.com";

        this.urlContent = new URLContentAnalyzer(fileName, url);
        this.urlContent.generateJSElements();
        this.urlContent.generateCSSElements();

        this.CSPGen = new CSPGenerator(this.urlContent);
    }

    @Test
    public void testGetCSPHeader() {
        this.CSPGen.generateCSPHeader();
        assertNotEquals("Content-Security-Policy: default-src 'self'; script-src 'self' ",
                this.CSPGen.getCSPHeader());
    }
}