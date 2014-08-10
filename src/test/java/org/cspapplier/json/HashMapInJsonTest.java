package org.cspapplier.json;

import org.cspapplier.HashMapGenerator;
import org.cspapplier.URLContentAnalyzer;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class HashMapInJsonTest {

    private URLContentAnalyzer getURL;
    private HashMapGenerator hashMap;
    private HashMapInJson hashMapInJson;

    @Before
    public void initialize() throws Exception {
        String fileName = "demo/index.html";
        this.getURL = new URLContentAnalyzer(fileName);
        getURL.generateJSElements();

        this.hashMap = new HashMapGenerator();
        this.hashMap.generateExternalHashMap(getURL.getExternalJSElements());
        this.hashMap.generateBlockHashMap(getURL.getBlockJSElements());
        this.hashMap.generateInlineHashMap(getURL.getInlineJSElementEvents());

        this.hashMapInJson = new HashMapInJson(this.hashMap.getExternalJSMap(),
                                               this.hashMap.getBlockJSMap(),
                                               this.hashMap.getInlineJSMap());
    }

    @Test
    public void testGetExternal() throws Exception {
        assertEquals(4, this.hashMapInJson.getExternal().size());

        String[] refSrcArray = new String[] {"http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js",
                                             "http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js",
                                             "http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js",
                                             "http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"};
        List<String> refSrc = Arrays.asList(refSrcArray);
        List<String> src = new ArrayList<String>();
        for (String id : this.hashMapInJson.getExternal().keySet()) {
            for (ElementInJson elementInJson : this.hashMapInJson.getExternal().get(id)) {
                src.add(elementInJson.getSrc());
            }
        }
        src.removeAll(refSrc);
        assertEquals(0, src.size());
    }

    @Test
    public void testGetBlock() throws Exception {
        assertEquals(5, this.hashMapInJson.getBlock().size());
    }

    @Test
    public void testGetInline() throws Exception {
        assertEquals(5, this.hashMapInJson.getInline().size());

        List<Integer> inlineNumbers = new ArrayList<Integer>();
        Integer[] refNumberArray = new Integer[] {1, 2, 2, 1, 1};
        List<Integer> refNumbers = Arrays.asList(refNumberArray);
        for (String id : this.hashMapInJson.getInline().keySet()) {
            inlineNumbers.add(hashMap.getInlineJSMap().get(id).size());
        }
        inlineNumbers.removeAll(refNumbers);
        assertEquals(0, inlineNumbers.size());
    }
}