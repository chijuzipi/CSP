package main.java.org.cspapplier;

import main.java.org.cspapplier.json.HashMapInJson;
import main.java.org.cspapplier.json.JsonAnalyzer;
import main.java.org.cspapplier.json.JsonWriter;
import main.java.org.cspapplier.util.SHAHash;

/**
 *  CSPApplier.java
 *
 *  The main class for processing the HTML/JS/CSS
 */

public class CSPApplier {
    public static void main(String[] args) throws Exception {
        String fileName = "demo/index.html";
        String hashURL = SHAHash.getHashCode("test.com");

        // Generate elements for external / block / inline JS
        URLContentAnalyzer getURL = new URLContentAnalyzer(fileName);
        getURL.generateJSElements();

        /*
         * Generate HashMaps for external / block / inline JS
         * - Key: the SHA1 hash of the JS content
         * - Value: the Elements or Array<ElementEventBinder> object
        */
        HashMapGenerator elementHashMap = new HashMapGenerator();
        elementHashMap.generateElementHashMap(getURL.getExternalJSElements(),
                                              getURL.getBlockJSElements(),
                                              getURL.getInlineJSElementEvents());

        /*
         * Generate Json compatible HashMaps for external / block / inline JS
         * - Key: the SHA1 hash of the JS content
         * - Value: the Array<ElementInJson> object which is Json compatible
        */
        HashMapInJson jsonFromRequest = new HashMapInJson(elementHashMap.getExternalJSMap(),
                                                          elementHashMap.getBlockJSMap(),
                                                          elementHashMap.getInlineJSMap());

        if (JsonAnalyzer.isLocalJsonExist(hashURL)) {
        	System.out.println("local template already exist!");
            HashMapInJson jsonFromLocal = JsonAnalyzer.jsonFromFile(hashURL);
            JsonAnalyzer jsonAnalyzer = new JsonAnalyzer(jsonFromRequest, jsonFromLocal);

            // Update the local json
            if (!jsonAnalyzer.isEmpty()) {
                jsonAnalyzer.updateLocalJson(jsonFromLocal);
                JsonWriter jsonWriter = new JsonWriter(jsonFromLocal, hashURL);
                jsonWriter.write();
                elementHashMap.filterHashMap(jsonAnalyzer.getExternalComparisonResult(),
                							  jsonAnalyzer.getBlockComparisonResult(),
                							  jsonAnalyzer.getInlineComparisonResult());
            }


        } else {
        	System.out.println("local template does not exist, this is the samping!");
            JsonWriter jsonWriter = new JsonWriter(jsonFromRequest, hashURL);
            jsonWriter.write();
            // FIXME: Generate HTML directly using HashMaps from elementHashMap
        }
        
        HTMLandJSGenerator htmlGen = new HTMLandJSGenerator(elementHashMap.getBlockJSMap(),
        										  elementHashMap.getInlineJSMap(),
        										  fileName);
        htmlGen.generateFile();
    
    }

}
