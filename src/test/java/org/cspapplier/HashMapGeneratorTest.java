package org.cspapplier;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class HashMapGeneratorTest {

    private URLContentAnalyzer getURL;
    private HashMapGenerator hashMap;

    @Before
    public void initialize() throws IOException, NoSuchAlgorithmException {
        String fileName = "src/test/resources/index.html";
        String url = "www.test.com";

        File html = new File(fileName);
        Document doc = Jsoup.parse(html, "UTF-8");

        this.getURL = new URLContentAnalyzer(doc.toString(), url);
        getURL.generateJSElements();
        getURL.generateCSSElements();

        this.hashMap = new HashMapGenerator();
    }

    @Test
    public void testGenerateExternalHashMap() throws Exception {
        hashMap.generateExternalJSHashMap(getURL.getExternalJSElements());
        assertEquals(4, hashMap.getExternalJSMap().size());
    }

    @Test
    public void testGenerateBlockHashMap() throws Exception {
        hashMap.generateBlockJSHashMap(getURL.getBlockJSElements());
        assertEquals(6, hashMap.getBlockJSMap().size());
    }

    @Test
    public void testGenerateInlineHashMap() throws Exception {
        hashMap.generateInlineJSHashMap(getURL.getInlineJSElementEvents());
        assertEquals(5, hashMap.getInlineJSMap().size());

        List<Integer> inlineNumbers = new ArrayList<Integer>();
        Integer[] refNumberArray = new Integer[] {1, 2, 2, 1, 1};
        List<Integer> refNumbers = Arrays.asList(refNumberArray);
        for (String id : hashMap.getInlineJSMap().keySet()) {
            inlineNumbers.add(hashMap.getInlineJSMap().get(id).size());
        }
        inlineNumbers.removeAll(refNumbers);
        assertEquals(0, inlineNumbers.size());
    }

    @Test
    public void testGenerateCSSElementsHashMap() throws Exception {
        hashMap.generateCSSElementHashMap(getURL);

        assertEquals(1, hashMap.getBlockCSSMap().size());
        assertEquals(2, hashMap.getInlineCSSMap().size());

        int numInline = 0;
        for (String id : hashMap.getInlineCSSMap().keySet()) {
            numInline += hashMap.getInlineCSSMap().get(id).size();
        }
        assertEquals(3, numInline);
    }
}