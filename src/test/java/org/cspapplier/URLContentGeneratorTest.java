package org.cspapplier;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class URLContentGeneratorTest {

    private URLContentGenerator urlContentGenerator;

    @Before
    public void initialize() throws IOException, NoSuchAlgorithmException {
        String fileName = "src/test/resources/index.html";
        String url = "www.test.com";
        String filePath = "src/test/resources/";
        String httpPath = "http://127.0.0.1";

        URLContentAnalyzer getURL = new URLContentAnalyzer(fileName, url);
        getURL.generateJSElements();
        getURL.generateCSSElements();

        HashMapGenerator hashMap = new HashMapGenerator();
        hashMap.generateJSElementHashMap(getURL);
        hashMap.generateCSSElementHashMap(getURL);

        this.urlContentGenerator = new URLContentGenerator(getURL, hashMap, httpPath, filePath);
    }

    @Test
    public void testGenerateElementID() throws NoSuchAlgorithmException {
        Set<String> testArray = new HashSet<String>();
        int testNumber = 1000;
        for (int i = 0; i < testNumber; i++) {
            testArray.add(urlContentGenerator.generateElementID());
        }

        assertEquals(testNumber, testArray.size());
    }

    @Test
    public void testHTTPPath() throws IOException {
        assertEquals("http://127.0.0.1/", urlContentGenerator.getHttpPath());
    }
}