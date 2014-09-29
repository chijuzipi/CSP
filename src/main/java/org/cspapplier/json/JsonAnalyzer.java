package org.cspapplier.json;

import com.google.gson.Gson;
import org.cspapplier.HashMapGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAnalyzer {
    private ComparisonResult jsComparisonResult;
    private ComparisonResult cssComparisonResult;

    public JsonAnalyzer(HashMapInJson jsonFromRequest, HashMapInJson jsonFromLocal) throws IOException {
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

        /* For each ID hashed from JS content:
         * 1. If the local json has the ID but elements not same, add to warning list with
         *    the list of different elements
         * 2. If the local json does not have the ID, add to black list.
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

    public void updateLocalJson(HashMapInJson localJson, boolean isSampling) {
        HashMap<String, DiffList> jsWarningList = jsComparisonResult.getWarningList();
        for (String id : jsWarningList.keySet()) {
            localJson.getJs().get(id).addAll(jsWarningList.get(id).getMissList());
        }

        HashMap<String, DiffList> cssWarningList = cssComparisonResult.getWarningList();
        for (String id : cssWarningList.keySet()) {
            localJson.getCss().get(id).addAll(cssWarningList.get(id).getMissList());
        }

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
    }

    public static boolean isLocalJsonExist(String hashURL) {
        File localJson = new File(hashURL + ".json");
        return localJson.exists();
    }

    public static HashMapInJson jsonFromFile(String hashURL) throws IOException {
        String inputFileName = hashURL + ".json";
        BufferedReader inputJsonBuffer = new BufferedReader(new FileReader(inputFileName));
        try {
            String inputJson;
            StringBuilder buildJson = new StringBuilder();
            while ((inputJson = inputJsonBuffer.readLine()) != null) {
                buildJson.append(inputJson);
            }

            Gson gson = new Gson();
            return gson.fromJson(buildJson.toString(), HashMapInJson.class);
        } finally {
            inputJsonBuffer.close();
        }
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
