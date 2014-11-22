package org.cspapplier;

import org.cspapplier.json.HashMapInJson;
import org.cspapplier.json.JsonAnalyzer;
import org.cspapplier.json.JsonWriter;

/**
 *  CSPApplier.java
 *
 *  The main class for processing the HTML/JS/CSS
 */

public class CSPApplier {
    public static void main(String[] args) throws Exception {
    	if (args.length != 5) {
    		System.out.println("Usage: Java CSPApplier [Input file] [Output file path] " +
                               "[HTTP Path] [URL] [isSampleMode = 0 | 1]");
    		return;
    	}

        String fileName = args[0];
        String outputPath = args[1];
        String httpPath = args[2];
        String url = args[3];
        boolean isSample = (Integer.parseInt(args[4]) != 0);

        // Generate elements for external / block / inline JS
        URLContentAnalyzer getURL = new URLContentAnalyzer(fileName, url, outputPath);
        getURL.generateJSElements();
        getURL.generateCSSElements();

        /*
         * Generate HashMaps for external / block / inline JS
         * - Key: the SHA1 hash of the JS content
         * - Value: the Elements or Array<ElementEventBinder> object
        */
        HashMapGenerator elementHashMap = new HashMapGenerator();
        elementHashMap.generateJSElementHashMap(getURL);
        elementHashMap.generateCSSElementHashMap(getURL);

        /*
         * Generate Json compatible HashMaps for external / block / inline JS
         * - Key: the SHA1 hash of the JS content
         * - Value: the Array<ElementInJson> object which is Json compatible
        */
        HashMapInJson jsonFromRequest = new HashMapInJson();
        jsonFromRequest.convertJS(elementHashMap);
        jsonFromRequest.convertCSS(elementHashMap);

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
        
        URLContentGenerator htmlGen = new URLContentGenerator(getURL, elementHashMap, httpPath);
        htmlGen.generateJS();
        htmlGen.generateCSS();
        htmlGen.generateHTML();
        
        CSPGenerator cspGen = new CSPGenerator(getURL);
        cspGen.generateCSPHeader();
        cspGen.write();
    }
}
