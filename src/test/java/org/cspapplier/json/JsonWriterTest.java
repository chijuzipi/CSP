package test.java.org.cspapplier.json;

import main.java.org.cspapplier.HashMapGenerator;
import main.java.org.cspapplier.URLContentAnalyzer;
import main.java.org.cspapplier.*;
import main.java.org.cspapplier.json.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriterTest {
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
    public void testWrite() throws Exception {
        JsonWriter jsonWriter = new JsonWriter(hashMapInJson, "demo/test");
        jsonWriter.write();
    }
}