package org.cspapplier;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class HashMapGeneratorTest {

    private URLContentAnalyzer getURL;
    private HashMapGenerator hashMap;

    @Before
    public void initialize() throws IOException {
        String fileName = "demo/index.html";
        this.getURL = new URLContentAnalyzer(fileName);
        getURL.generateJSElements();

        this.hashMap = new HashMapGenerator();
    }

    @Test
    public void testGenerateExternalHashMap() throws Exception {
        hashMap.generateExternalHashMap(getURL.getExternalJSElements());
        assertEquals(4, hashMap.getExternalJSMap().size());
    }

    @Test
    public void testGenerateBlockHashMap() throws Exception {
        hashMap.generateBlockHashMap(getURL.getBlockJSElements());
        assertEquals(5, hashMap.getBlockJSMap().size());
    }

    @Test
    public void testGenerateInlineHashMap() throws Exception {
        hashMap.generateInlineHashMap(getURL.getInlineJSElementEvents());
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
}