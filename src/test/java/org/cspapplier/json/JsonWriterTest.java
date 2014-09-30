package org.cspapplier.json;

import org.cspapplier.HashMapGenerator;
import org.cspapplier.URLContentAnalyzer;
import org.junit.Before;
import org.junit.Test;

public class JsonWriterTest {
    private URLContentAnalyzer getURL;
    private HashMapInJson hashMapInJson;

    @Before
    public void initialize() throws Exception {
        String fileName = "src/test/resources/index.html";
        String url = "www.test.com";
        String outputPath = "src/test/resources";

        this.getURL = new URLContentAnalyzer(fileName, url, outputPath);
        getURL.generateJSElements();
        getURL.generateCSSElements();

        HashMapGenerator hashMap = new HashMapGenerator();
        hashMap.generateJSElementHashMap(getURL);
        hashMap.generateCSSElementHashMap(getURL);

        this.hashMapInJson = new HashMapInJson();
        this.hashMapInJson.convertJS(hashMap);
        this.hashMapInJson.convertCSS(hashMap);
    }
    @Test
    public void testWrite() throws Exception {
        JsonWriter jsonWriter = new JsonWriter(hashMapInJson, getURL.getHashURL(), getURL.getOutputPath());
        jsonWriter.write();
    }
}