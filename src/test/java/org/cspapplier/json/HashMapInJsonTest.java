package org.cspapplier.json;

import org.cspapplier.HashMapGenerator;
import org.cspapplier.URLContentAnalyzer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class HashMapInJsonTest {

    private HashMapGenerator hashMap;
    private HashMapInJson hashMapInJson;

    @Before
    public void initialize() throws Exception {
        String fileName = "src/test/resources/index.html";
        String url = "www.test.com";
        URLContentAnalyzer getURL = new URLContentAnalyzer(fileName, url);
        getURL.generateJSElements();
        getURL.generateCSSElements();

        this.hashMap = new HashMapGenerator();
        this.hashMap.generateJSElementHashMap(getURL);
        this.hashMap.generateCSSElementHashMap(getURL);

        this.hashMapInJson = new HashMapInJson();
    }

    @Test
    public void testConvertJS() throws Exception {
        this.hashMapInJson.convertJS(hashMap);

        // Test the total number of js
        assertEquals(15, this.hashMapInJson.getJs().size());

        // Test the content of src from external js
        String[] refSrcArray = new String[] {"http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js",
                                             "http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js",
                                             "http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js",
                                             "http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"};
        List<String> refSrc = Arrays.asList(refSrcArray);
        List<String> src = new ArrayList<String>();
        for (String id : this.hashMapInJson.getJs().keySet()) {
            for (ElementInJson elementInJson : this.hashMapInJson.getJs().get(id)) {
                if (!elementInJson.getSrc().equals("")) {
                    src.add(elementInJson.getSrc());
                }
            }
        }

        src.removeAll(refSrc);
        assertEquals(0, src.size());

        // Test the number of block JS
        int numBlockJS = 0;
        for (String id : this.hashMapInJson.getJs().keySet()) {
            for (ElementInJson elementInJson : this.hashMapInJson.getJs().get(id)) {
                if (elementInJson.getTag().equals("script") &&
                    elementInJson.getSrc().equals("")) {
                    numBlockJS += 1;
                }
            }
        }
        assertEquals(6, numBlockJS);

        // Test the number of inline JS
        int numInlineJS = 0;
        List<Integer> inlineNumbers = new ArrayList<Integer>();
        Integer[] refNumberArray = new Integer[] {1, 2, 2, 1, 1};
        List<Integer> refNumbers = Arrays.asList(refNumberArray);
        for (String id : this.hashMapInJson.getJs().keySet()) {
            if (!hashMapInJson.getJs().get(id).get(0).getEvent().equals("")) {
                numInlineJS += 1;
                inlineNumbers.add(hashMap.getInlineJSMap().get(id).size());
            }
        }
        inlineNumbers.removeAll(refNumbers);

        assertEquals(5, numInlineJS);
        assertEquals(0, inlineNumbers.size());
    }

    @Test
    public void testConvertCSS() {
        this.hashMapInJson.convertCSS(hashMap);

        assertEquals(4, this.hashMapInJson.getCss().size());

        int numInline = 0;
        for (String id : this.hashMapInJson.getCss().keySet()) {
            for (ElementInJson elementInJson : hashMapInJson.getCss().get(id)) {
                if (!elementInJson.getTag().equals("style")) {
                    numInline += 1;
                }
            }
        }
        assertEquals(4, numInline);
    }
}