package org.cspapplier.json;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.cspapplier.HashMapGenerator;
import org.cspapplier.URLContentAnalyzer;

import org.cspapplier.mongo.PageJsonColl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class JsonAnalyzerTest {

    private MongoDatabase db;
    private PageJsonColl pageJsonColl;
    private URLContentAnalyzer getURL;
    private HashMapGenerator hashMap;
    private HashMapInJson currentJson;

    @Before
    public void initialize() throws Exception {
        String path = "src/test/resources/";
        String fileName = path + "index.html";
        String url = "www.test.com";
        /**
         * Initialize the database
         */
        MongoClientURI dbURI = new MongoClientURI("mongodb://127.0.0.1:27017");
        MongoClient dbClient = new MongoClient(dbURI);
        db = dbClient.getDatabase("test");
        pageJsonColl = new PageJsonColl(db);

        File html = new File(fileName);
        Document doc = Jsoup.parse(html, "UTF-8");

        getURL = new URLContentAnalyzer(doc.toString(), url);
        getURL.generateJSElements();
        getURL.generateCSSElements();

        this.hashMap = new HashMapGenerator();
        this.hashMap.generateJSElementHashMap(getURL);
        this.hashMap.generateCSSElementHashMap(getURL);

        this.currentJson = new HashMapInJson();
        this.currentJson.convertJS(this.hashMap);
        this.currentJson.convertCSS(this.hashMap);

        String jsonFilePath = path + getURL.getHashURL() + ".modified.json";
        byte[] encoded = Files.readAllBytes(Paths.get(jsonFilePath));
        String jsonString = new String(encoded, Charset.defaultCharset());
        pageJsonColl.insert(getURL.getHashURL(), jsonString);
    }

    @Test
    public void testIsLocalJsonExist() {
        assertTrue(JsonAnalyzer.isLocalJsonExist(getURL.getHashURL(), pageJsonColl));
        assertTrue(!JsonAnalyzer.isLocalJsonExist("NotExist", pageJsonColl));
    }

    @Test
    public void testJsonFromLocal() throws IOException {
        HashMapInJson localJson = JsonAnalyzer.jsonFromLocal(getURL.getHashURL(), pageJsonColl);
        assertEquals(15, localJson.getJs().size());
        assertEquals(4, localJson.getCss().size());
    }

    @Test
    public void testGetJSComparisonResult() {
        HashMapInJson localJson = JsonAnalyzer.jsonFromLocal(getURL.getHashURL(), pageJsonColl);
        JsonAnalyzer jsonAnalyzer = new JsonAnalyzer(currentJson, localJson);

        HashMap<String, ArrayList<ElementInJson>> blackList = jsonAnalyzer.getJsComparisonResult().getBlackList();
        HashMap<String, DiffList> warningList = jsonAnalyzer.getJsComparisonResult().getWarningList();

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
    public void testGetCSSComparisonResult() {
        HashMapInJson localJson = JsonAnalyzer.jsonFromLocal(getURL.getHashURL(), pageJsonColl);
        JsonAnalyzer jsonAnalyzer = new JsonAnalyzer(currentJson, localJson);

        HashMap<String, ArrayList<ElementInJson>> blackList = jsonAnalyzer.getCssComparisonResult().getBlackList();
        HashMap<String, DiffList> warningList = jsonAnalyzer.getCssComparisonResult().getWarningList();

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
        HashMapInJson localJson = JsonAnalyzer.jsonFromLocal(getURL.getHashURL(), pageJsonColl);
        JsonAnalyzer jsonAnalyzer = new JsonAnalyzer(currentJson, localJson);
        jsonAnalyzer.updateLocalJson(localJson, false, getURL.getHashURL(), pageJsonColl);

        assertEquals(15, localJson.getJs().size());
        assertEquals(3, localJson.getJs().get("254cef50c5977a86c69a9f56ffee64496ddf99b5").size());
    }

    @Test
    public void testFilterHashMap() {
        HashMapInJson localJson = JsonAnalyzer.jsonFromLocal(getURL.getHashURL(), pageJsonColl);
        JsonAnalyzer jsonAnalyzer = new JsonAnalyzer(currentJson, localJson);
        jsonAnalyzer.filterHashMap(this.hashMap);
        assertEquals(4, this.hashMap.getInlineJSMap().size());
    }

    @After
    public void cleanUp() {
        db.drop();
    }
}