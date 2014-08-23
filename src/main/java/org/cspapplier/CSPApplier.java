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
    	if(args.length != 2){
    		System.out.println("Usage: Java CSPApplier [.html file path] [url]");
    		return;
    	}
        String fileName = args[0];
        String url = args[1];

        // Generate elements for external / block / inline JS
        URLContentAnalyzer getURL = new URLContentAnalyzer(fileName, url);
        getURL.generateJSElements();
        getURL.generateCSSElements();

        /*
         * Generate HashMaps for external / block / inline JS
         * - Key: the SHA1 hash of the JS content
         * - Value: the Elements or Array<ElementEventBinder> object
        */
        HashMapGenerator elementHashMap = new HashMapGenerator();
        elementHashMap.generateJSElementHashMap(getURL);
        elementHashMap.generateCSSElementHashmap(getURL);

        /*
         * Generate Json compatible HashMaps for external / block / inline JS
         * - Key: the SHA1 hash of the JS content
         * - Value: the Array<ElementInJson> object which is Json compatible
        */
        HashMapInJson jsonFromRequest = new HashMapInJson();
        jsonFromRequest.convertJS(elementHashMap);
        jsonFromRequest.convertCSS(elementHashMap);

        if (JsonAnalyzer.isLocalJsonExist(getURL.getHashURL())) {
            System.out.println("Local template already exist!");
            HashMapInJson jsonFromLocal = JsonAnalyzer.jsonFromFile(getURL.getHashURL());
            JsonAnalyzer jsonAnalyzer = new JsonAnalyzer(jsonFromRequest, jsonFromLocal);

            // Update the local json and the HashMaps containing elements.
            if (!jsonAnalyzer.isEmpty()) {
                jsonAnalyzer.updateLocalJson(jsonFromLocal);
                JsonWriter jsonWriter = new JsonWriter(jsonFromLocal, getURL.getHashURL());
                jsonWriter.write();
                
                //the hash maps in HashMapGenerator is updated by the comparison result 
                jsonAnalyzer.filterHashMap(elementHashMap);
            }

        } else {
            System.out.println("Local template does not exist! Generating new template...");
            JsonWriter jsonWriter = new JsonWriter(jsonFromRequest, getURL.getHashURL());
            jsonWriter.write();
        }
        
        URLContentGenerator htmlGen = new URLContentGenerator(getURL, elementHashMap);
        htmlGen.generateJS();
        htmlGen.generateCSS();
        htmlGen.generateHTML();
        
        CSPGenerator cspGen = new CSPGenerator(getURL);
        String cspHeader = cspGen.generateCSPHeader();
        System.out.println("CSP Header is : " + cspHeader); 
    }
}
