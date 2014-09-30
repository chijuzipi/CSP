package org.cspapplier;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CSPGeneratorTest {

    CSPGenerator CSPGen;
    URLContentAnalyzer urlContent;

    @Before
    public void setUp() throws Exception {
        String outputPath = "src/test/resources/";
        String fileName = outputPath + "index.html";
        String url = "www.test.com";

        this.urlContent = new URLContentAnalyzer(fileName, url, outputPath);
        this.urlContent.generateJSElements();
        this.urlContent.generateCSSElements();

        this.CSPGen = new CSPGenerator(this.urlContent);
    }

    @Test
    public void testWrite() throws Exception {
        this.CSPGen.generateCSPHeader();
        this.CSPGen.write();
    }
}