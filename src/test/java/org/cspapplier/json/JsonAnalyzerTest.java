package org.cspapplier.json;

import org.cspapplier.HashMapGenerator;
import org.cspapplier.URLContentAnalyzer;
import org.junit.Before;
import org.junit.Test;

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
    private HashMapInJson hashMapInJson;
    private HashMapInJson localJson;
    private JsonAnalyzer jsonAnalyzer;

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
        this.localJson = JsonAnalyzer.jsonFromFile("demo/testRef");
        this.jsonAnalyzer = new JsonAnalyzer(this.hashMapInJson, this.localJson);
    }

    @Test
    public void testIsLocalJsonExist() throws Exception {
        String fileExistName = "demo/testRef";
        String fileNotExistName = "demo/Ref";
        assertTrue(JsonAnalyzer.isLocalJsonExist(fileExistName));
        assertTrue(!JsonAnalyzer.isLocalJsonExist(fileNotExistName));
    }

    @Test
    public void testJsonFromFile() throws IOException {
        HashMapInJson localJson = JsonAnalyzer.jsonFromFile("demo/testRef");
        assertEquals(4, localJson.getExternal().size());
        assertEquals(5, localJson.getBlock().size());
        assertEquals(5, localJson.getInline().size());
    }

    @Test
    public void testGetInlineComparisonResult() throws Exception {
        ArrayList<String> blackList = this.jsonAnalyzer.getInlineComparisonResult().getBlackList();
        HashMap<String, DiffList> warningList = this.jsonAnalyzer.getInlineComparisonResult().getWarningList();

        assertEquals(1, blackList.size());
        assertTrue(blackList.contains("2e962c6181d7a15c0f3ddd43e9c6cf202aee9556"));

        assertEquals(2, warningList.size());

        String[] refWarningArray = new String[] {"2afca66e70fd92a12f593cd6f02a71b19dd2458f",
                                                 "99d78af41bf6b89943c2587491621e53aa7f57c1"};
        List<String> refWarning = Arrays.asList(refWarningArray);
        List<String> warning = new ArrayList<String>(warningList.keySet());
        warning.removeAll(refWarning);
        assertEquals(0, warning.size());

        assertEquals(1, warningList.get("99d78af41bf6b89943c2587491621e53aa7f57c1").getMissList().size());
        String event1 = warningList.get("99d78af41bf6b89943c2587491621e53aa7f57c1").getMissList().get(0).getEvent();
        assertEquals("onmouseup", event1);

        assertEquals(1, warningList.get("99d78af41bf6b89943c2587491621e53aa7f57c1").getMoreList().size());
        String event2 = warningList.get("99d78af41bf6b89943c2587491621e53aa7f57c1").getMoreList().get(0).getEvent();
        assertEquals("onmouseover", event2);
    }
}