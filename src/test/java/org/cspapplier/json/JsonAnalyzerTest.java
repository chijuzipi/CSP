package org.cspapplier.json;

import org.cspapplier.HashMapGenerator;
import org.cspapplier.URLContentAnalyzer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class JsonAnalyzerTest {

    private URLContentAnalyzer getURL;
    private HashMapGenerator hashMap;
    private JsonAnalyzer jsonAnalyzer;
    private HashMapInJson localJson;

    @Before
    public void initialize() throws Exception {
        String path = "src/test/resources/";
        String fileName = path + "index.html";
        String url = "www.test.com";

        File html = new File(fileName);
        Document doc = Jsoup.parse(html, "UTF-8");

        getURL = new URLContentAnalyzer(doc.toString(), url);
        getURL.generateJSElements();
        getURL.generateCSSElements();

        this.hashMap = new HashMapGenerator();
        this.hashMap.generateJSElementHashMap(getURL);
        this.hashMap.generateCSSElementHashMap(getURL);

        HashMapInJson hashMapInJson = new HashMapInJson();
        hashMapInJson.convertJS(this.hashMap);
        hashMapInJson.convertCSS(this.hashMap);
        localJson = JsonAnalyzer.jsonFromLocal(getURL.getHashURL() + ".modified", getURL.getOutputPath());
        this.jsonAnalyzer = new JsonAnalyzer(hashMapInJson, localJson);
    }

    @Test
    public void testIsLocalJsonExist() throws Exception {
        String fileExistName = getURL.getHashURL() + ".orig";
        String fileNotExistName = "notExist";
        assertTrue(JsonAnalyzer.isLocalJsonExist(fileExistName, getURL.getOutputPath()));
        assertTrue(!JsonAnalyzer.isLocalJsonExist(fileNotExistName, getURL.getOutputPath()));
    }

    @Test
    public void testJsonFromLocal() throws IOException {
        HashMapInJson localJson = JsonAnalyzer.jsonFromLocal(getURL.getHashURL() + ".modified", getURL.getOutputPath());
        assertEquals(15, localJson.getJs().size());
        assertEquals(4, localJson.getCss().size());
    }

    @Test
    public void testGetJSComparisonResult() throws Exception {
        HashMap<String, ArrayList<ElementInJson>> blackList = this.jsonAnalyzer.getJsComparisonResult().getBlackList();
        HashMap<String, DiffList> warningList = this.jsonAnalyzer.getJsComparisonResult().getWarningList();

        assertEquals(1, blackList.size());
        assertTrue(blackList.keySet().contains("aac7b7bc5c401fa96fd1c403bfcd257e5c5b900b"));

        assertEquals(2, warningList.size());

        String[] refWarningArray = new String[] {"254cef50c5977a86c69a9f56ffee64496ddf99b5",
                                                 "c8ff7c2f36af85d78d061cbd3ab16edb5f9d3f66"};
        List<String> refWarning = Arrays.asList(refWarningArray);
        List<String> warning = new ArrayList<String>(warningList.keySet());
        warning.removeAll(refWarning);
        assertEquals(0, warning.size());

        assertEquals(1, warningList.get("254cef50c5977a86c69a9f56ffee64496ddf99b5").getMissList().size());
        String event1 = warningList.get("254cef50c5977a86c69a9f56ffee64496ddf99b5").getMissList().get(0).getEvent();
        assertEquals("onmouseup", event1);

        assertEquals(1, warningList.get("254cef50c5977a86c69a9f56ffee64496ddf99b5").getMoreList().size());
        String event2 = warningList.get("254cef50c5977a86c69a9f56ffee64496ddf99b5").getMoreList().get(0).getEvent();
        assertEquals("onmouseover", event2);
    }

    @Test
    public void testGetCSSComparisonResult() throws Exception {
        HashMap<String, ArrayList<ElementInJson>> blackList = this.jsonAnalyzer.getCssComparisonResult().getBlackList();
        HashMap<String, DiffList> warningList = this.jsonAnalyzer.getCssComparisonResult().getWarningList();

        assertEquals(1, blackList.size());
        assertTrue(blackList.keySet().contains("a090361fcef19a9ac19e96fa40083a1a89182621"));

        assertEquals(1, warningList.size());

        String[] refWarningArray = new String[] {"1cbfaf12ea8cff56ff1eaae9fcebafba44e68afb"};
        List<String> refWarning = Arrays.asList(refWarningArray);
        List<String> warning = new ArrayList<String>(warningList.keySet());
        warning.removeAll(refWarning);
        assertEquals(0, warning.size());

        assertEquals(1, warningList.get("1cbfaf12ea8cff56ff1eaae9fcebafba44e68afb").getMissList().size());
        String tag1 = warningList.get("1cbfaf12ea8cff56ff1eaae9fcebafba44e68afb").getMissList().get(0).getTag();
        assertEquals("div", tag1);

        assertEquals(1, warningList.get("1cbfaf12ea8cff56ff1eaae9fcebafba44e68afb").getMoreList().size());
    }

    @Test
    public void testUpdateLocalJson() {
        this.jsonAnalyzer.updateLocalJson(this.localJson, false);

        assertEquals(15, localJson.getJs().size());
        assertEquals(3, this.localJson.getJs().get("254cef50c5977a86c69a9f56ffee64496ddf99b5").size());
    }

    @Test
    public void testFilterHashMap() {
        this.jsonAnalyzer.filterHashMap(this.hashMap);
        assertEquals(4, this.hashMap.getInlineJSMap().size());
    }
}