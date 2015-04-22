package org.cspapplier;

import org.cspapplier.json.HashMapInJson;
import org.cspapplier.json.JsonAnalyzer;

/**
 *  CSPApplier.java
 *
 *  The main class for processing the HTML/JS/CSS
 *  - inputHTML: The HTML response from the proxy
 *  - url: The URL of the request
 *  - outputPath: The local path to save the new JS/CSS files
 *  - httpPath: The HTTP URL to refer to the new JS/CSS files
 *  - isSample: The switcher for sampling mode
 */

public class CSPApplier {
    private String httpPath;
    private String filePath;

    /**
     *  getURL: The analyzer for the HTML content
     *  htmlGen: The generator for the new HTML/JS/CSS
     *  cspGen: The generator for the CSP header
     */
    private URLContentAnalyzer getURL;
    private URLContentGenerator htmlGen;

    private HashMapGenerator elementHashMap;
    private HashMapInJson jsonFromRequest;

    private boolean isSample;

    public CSPApplier(String inputHTML, String url, String filePath,
                      String httpPath, boolean isSample) throws Exception {
        /**
         *  Query the elements with external / block / inline JS
         */
        getURL = new URLContentAnalyzer(inputHTML, url);
        getURL.generateJSElements();
        getURL.generateCSSElements();

        elementHashMap = new HashMapGenerator();
        jsonFromRequest = new HashMapInJson();

        this.httpPath = httpPath;
        this.filePath = filePath;
        this.isSample = isSample;
    }

    public void analyzeJson() throws Exception {
        /**
         * Generate HashMaps for external / block / inline JS
         * - Key: the SHA1 hash of the JS content
         * - Value: the Elements or Array<ElementEventBinder> object
         */
        elementHashMap.generateJSElementHashMap(getURL);
        elementHashMap.generateCSSElementHashMap(getURL);

        /**
         * Generate Json compatible HashMaps for external / block / inline JS
         * - Key: the SHA1 hash of the JS content
         * - Value: the Array<ElementInJson> object which is Json compatible
         */
        jsonFromRequest.convertJS(elementHashMap);
        jsonFromRequest.convertCSS(elementHashMap);

        /**
         * Generate JSON template
         */
        if (JsonAnalyzer.isLocalJsonExist(getURL.getHashURL(), filePath)) {
            System.out.println("Local template already exist!");
            HashMapInJson jsonFromLocal = JsonAnalyzer.jsonFromFile(getURL.getHashURL(), filePath);
            JsonAnalyzer jsonAnalyzer = new JsonAnalyzer(jsonFromRequest, jsonFromLocal);

            /**
             *  Update the local json and filter the elements in the hashmaps
             */
            if (!jsonAnalyzer.isEmpty()) {
                jsonAnalyzer.updateLocalJson(jsonFromLocal, isSample);
                JsonWriter jsonWriter = new JsonWriter(jsonFromLocal, getURL.getHashURL(), filePath);
                jsonWriter.write();

                jsonAnalyzer.filterHashMap(elementHashMap);
            }

        } else {
            System.out.println("Local template does not exist! Generating new template...");
            JsonWriter jsonWriter = new JsonWriter(jsonFromRequest, getURL.getHashURL(), filePath);
            jsonWriter.write();
        }

        /**
         * Use the filtered hashmap to generate new JS/CSS
         */
        htmlGen = new URLContentGenerator(getURL, elementHashMap, httpPath, filePath);
    }

    /**
     * Generate new HTML and pass to the proxy
     */
    public String generateHTML() throws Exception {
        return htmlGen.generateHTML();
    }

    /**
     * Write new JS to file
     */
    public void generateJS() throws Exception {
        htmlGen.generateJS();
    }

    /**
     * Write new CSS to file
     */
    public void generateCSS() throws Exception {
        htmlGen.generateCSS();
    }

    /**
     * Generate CSP header and pass to proxy
     */
    public String generateCSP() {
        CSPGenerator cspGen = new CSPGenerator(getURL);
        cspGen.generateCSPHeader();
        return cspGen.getCSPHeader();
    }
}
