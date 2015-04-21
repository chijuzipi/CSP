package org.cspapplier;

import org.cspapplier.json.HashMapInJson;
import org.cspapplier.json.JsonAnalyzer;
import org.cspapplier.json.JsonWriter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 *  CSPApplier.java
 *
 *  The main class for processing the HTML/JS/CSS
 */

//    	if (args.length != 5) {
//    		System.out.println("Usage: Java CSPApplier [Input file] [Output file path] " +
//                               "[HTTP Path] [URL] [isSampleMode = 0 | 1]");
//    		return;
//    	}

public class CSPApplier {
    URLContentAnalyzer getURL;
    URLContentGenerator htmlGen;
    CSPGenerator cspGen;

    HashMapGenerator elementHashMap;
    HashMapInJson jsonFromRequest;

    public CSPApplier(String inputHTML, String url, String outputPath, String httpPath) throws Exception {
        /*
         *  Query the elements with external / block / inline JS
         */
        getURL = new URLContentAnalyzer(inputHTML, url, outputPath);
        getURL.generateJSElements();
        getURL.generateCSSElements();

        elementHashMap = new HashMapGenerator();
        jsonFromRequest = new HashMapInJson();
    }

    public void analyzeJson() throws Exception {
        /*
         * Generate HashMaps for external / block / inline JS
         * - Key: the SHA1 hash of the JS content
         * - Value: the Elements or Array<ElementEventBinder> object
         */
        elementHashMap.generateJSElementHashMap(getURL);
        elementHashMap.generateCSSElementHashMap(getURL);

        /*
         * Generate Json compatible HashMaps for external / block / inline JS
         * - Key: the SHA1 hash of the JS content
         * - Value: the Array<ElementInJson> object which is Json compatible
         */
        jsonFromRequest.convertJS(elementHashMap);
        jsonFromRequest.convertCSS(elementHashMap);

        /*
         * Generate JSON template
         */

        if (JsonAnalyzer.isLocalJsonExist(getURL.getHashURL(), getURL.getOutputPath())) {
            System.out.println("Local template already exist!");
            HashMapInJson jsonFromLocal = JsonAnalyzer.jsonFromFile(getURL.getHashURL(), getURL.getOutputPath());
            JsonAnalyzer jsonAnalyzer = new JsonAnalyzer(jsonFromRequest, jsonFromLocal);

            // Update the local json and the HashMaps containing elements.
            if (!jsonAnalyzer.isEmpty()) {
                jsonAnalyzer.updateLocalJson(jsonFromLocal, isSample);
                JsonWriter jsonWriter = new JsonWriter(jsonFromLocal, getURL.getHashURL(), getURL.getOutputPath());
                jsonWriter.write();

                jsonAnalyzer.filterHashMap(elementHashMap);
            }

        } else {
            System.out.println("Local template does not exist! Generating new template...");
            JsonWriter jsonWriter = new JsonWriter(jsonFromRequest, getURL.getHashURL(), getURL.getOutputPath());
            jsonWriter.write();
        }

        /*
         * Write new HTML/JS/CSS files to the files
         */
        htmlGen = new URLContentGenerator(getURL, elementHashMap, httpPath);
    }

    public String generateHTML() throws Exception {
        htmlGen.generateHTML();
        return newHTML;
    }

    public void generateJS() throws Exception {
        htmlGen.generateJS();
    }

    public void generateCSS() throws Exception {
        htmlGen.generateCSS();
    }

    public String generateCSP() {
        /*
         * Generate CSP header
         */
        cspGen = new CSPGenerator(getURL);
        cspGen.generateCSPHeader();
        cspGen.write();
        return cspHeader;
    }
}
