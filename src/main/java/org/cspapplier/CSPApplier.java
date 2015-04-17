package org.cspapplier;

import org.cspapplier.json.HashMapInJson;
import org.cspapplier.json.JsonAnalyzer;
import org.cspapplier.mongo.PageJsonColl;

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

        String hashURL = getURL.getHashURL();
        if (JsonAnalyzer.isLocalJsonExist(hashURL, PageJsonColl)) {
            System.out.println("Local template already exist!");
            HashMapInJson jsonFromLocal = JsonAnalyzer.jsonFromFile(hashURL, PageJsonColl);
            JsonAnalyzer jsonAnalyzer = new JsonAnalyzer(jsonFromRequest, jsonFromLocal);

            // Found difference, update the local json and the HashMaps containing elements.
            if (!jsonAnalyzer.isEmpty()) {

                //change the jsonFromLocal object, also update database
                jsonAnalyzer.updateLocalJson(jsonFromLocal, isSample, hashURL, PageJsonColl);

                //
                jsonAnalyzer.filterHashMap(elementHashMap);
            }

        } else {
            System.out.println("Local template does not exist! Generating new template...");
            JsonAnalyzer.insertNewJson(getURL.getHashURL(), PageJsonColl, jsonFromRequest);
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
