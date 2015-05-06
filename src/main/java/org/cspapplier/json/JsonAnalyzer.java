package org.cspapplier.json;

import com.google.gson.Gson;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.FindIterable;
import org.bson.Document;

import org.cspapplier.HashMapGenerator;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

import org.cspapplier.mongo.PageJsonColl;

public class JsonAnalyzer {
    private ComparisonResult jsComparisonResult;
    private ComparisonResult cssComparisonResult;

    public JsonAnalyzer(HashMapInJson jsonFromRequest, HashMapInJson jsonFromLocal) {
        this.jsComparisonResult = generateFilterList(jsonFromRequest.getJs(), jsonFromLocal.getJs());
        this.cssComparisonResult = generateFilterList(jsonFromRequest.getCss(), jsonFromLocal.getCss());
    }

    private ComparisonResult generateFilterList(HashMap<String, ArrayList<ElementInJson>> requestHM,
                                                HashMap<String, ArrayList<ElementInJson>> localHM) {
        HashMap<String, DiffList> warningList = new HashMap<String, DiffList>();
        HashMap<String, ArrayList<ElementInJson>> blackList = new HashMap<String, ArrayList<ElementInJson>>();

        ArrayList<ElementInJson> elementsInRequestJson;
        ArrayList<ElementInJson> elementsInLocalJson;

        ArrayList<ElementInJson> elementsMiss;
        ArrayList<ElementInJson> elementsMore;
        DiffList diffList;

        /**
         *  For each ID hashed from JS content:
         *  - If the local json has the ID but elements not same, add to warning list with
         *    the list of different elements
         *  - If the local json does not have the ID, add to black list.
         */
        for (String id : requestHM.keySet()) {
            if (localHM.containsKey(id)) {
                elementsInRequestJson = requestHM.get(id);
                elementsInLocalJson = localHM.get(id);

                if (!elementsInRequestJson.equals(elementsInLocalJson)) {
                    diffList = new DiffList();
                    elementsMiss = diffList.getMissList();
                    elementsMore = diffList.getMoreList();

                    elementsMiss.addAll(elementsInLocalJson);
                    elementsMiss.removeAll(elementsInRequestJson);

                    elementsMore.addAll(elementsInRequestJson);
                    elementsMore.removeAll(elementsInLocalJson);

                    diffList.setMissList(elementsMiss);
                    diffList.setMoreList(elementsMore);

                    warningList.put(id, diffList);
                }
            } else {
                blackList.put(id, requestHM.get(id));
            }
        }

        return new ComparisonResult(warningList, blackList);
    }

    public boolean isEmpty() {
        return this.jsComparisonResult.isEmpty() &&
               this.cssComparisonResult.isEmpty();
    }

    public void updateLocalJson(HashMapInJson localJson, boolean isSampling, String hashURL, PageJsonColl pageJson) {
        /**
         * Add the warning element list to the local json
         */
        HashMap<String, DiffList> jsWarningList = jsComparisonResult.getWarningList();
        for (String id : jsWarningList.keySet()) {
            localJson.getJs().get(id).addAll(jsWarningList.get(id).getMissList());
        }

        HashMap<String, DiffList> cssWarningList = cssComparisonResult.getWarningList();
        for (String id : cssWarningList.keySet()) {
            localJson.getCss().get(id).addAll(cssWarningList.get(id).getMissList());
        }

        /**
         * If sampling mode is ON, add the blacklist element list to the local json
         */
        if (isSampling) {
            HashMap<String, ArrayList<ElementInJson>> jsBlackList = jsComparisonResult.getBlackList();
            for (String id : jsBlackList.keySet()) {
                localJson.getJs().put(id, jsBlackList.get(id));
            }

            HashMap<String, ArrayList<ElementInJson>> cssBlackList = cssComparisonResult.getBlackList();
            for (String id : cssBlackList.keySet()) {
                localJson.getCss().put(id, cssBlackList.get(id));
            }
        }

        updateDBJson(hashURL, pageJson, localJson);
    }

    /*
     * test if the template exist in the local db (pageJson collection)
     */
    public static boolean isLocalJsonExist(String hashURL, PageJsonColl pageJson) {
        Document myDoc = (Document)(pageJson.getCollection().find(eq("URLHash", hashURL)).first());
        return (myDoc != null);
    }

    public static void insertNewJson(String hashURL, PageJsonColl pageJson, HashMapInJson jsonFromRequest){
        pageJson.insert(hashURL, jsonFromRequest.toString());
    }

    public void updateDBJson(String hashURL, PageJsonColl pageJson, HashMapInJson newJson){
        pageJson.getCollection().updateOne(eq("URLHash", hashURL), new Document("$set", new Document("URLHash", newJson)));
    }


    public static HashMapInJson jsonFromLocal(String hashURL, PageJsonColl pageJson) {
            Document myDoc = (Document)(pageJson.getCollection()
                    .find(eq("URLHash", hashURL))
                    .first());

            Gson gson = new Gson();
            return gson.fromJson(myDoc.toJson(), HashMapInJson.class);
    }

    public void filterHashMap(HashMapGenerator hashMaps) {
        for (String key : this.jsComparisonResult.getBlackList().keySet()) {
            hashMaps.getExternalJSMap().remove(key);
            hashMaps.getBlockJSMap().remove(key);
            hashMaps.getInlineJSMap().remove(key);
        }

        for (String key : this.cssComparisonResult.getBlackList().keySet()) {
            hashMaps.getBlockCSSMap().remove(key);
            hashMaps.getInlineCSSMap().remove(key);
        }
    }

    public ComparisonResult getJsComparisonResult() {
        return this.jsComparisonResult;
    }

    public void setJsComparisonResult(ComparisonResult jsComparisonResult) {
        this.jsComparisonResult = jsComparisonResult;
    }

    public ComparisonResult getCssComparisonResult() {
        return this.cssComparisonResult;
    }

    public void setCssComparisonResult(ComparisonResult cssComparisonResult) {
        this.cssComparisonResult = cssComparisonResult;
    }
}
