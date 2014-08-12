package main.java.org.cspapplier.json;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonAnalyzer {
    private ComparisonResult externalComparisonResult;
    private ComparisonResult blockComparisonResult;
    private ComparisonResult inlineComparisonResult;

    public JsonAnalyzer(HashMapInJson jsonFromRequest, HashMapInJson jsonFromLocal) throws IOException {
        compareJson(jsonFromRequest, jsonFromLocal);
    }

    private void compareJson(HashMapInJson jsonFromRequest, HashMapInJson jsonFromLocal) {
        this.externalComparisonResult = generateFilterList(jsonFromRequest.getExternal(),
                                                           jsonFromLocal.getExternal());
        this.blockComparisonResult = generateFilterList(jsonFromRequest.getBlock(),
                                                        jsonFromLocal.getBlock());
        this.inlineComparisonResult = generateFilterList(jsonFromRequest.getInline(),
                                                         jsonFromLocal.getInline());
    }

    private ComparisonResult generateFilterList(HashMap<String, ArrayList<ElementInJson>> requestHM,
                                                HashMap<String, ArrayList<ElementInJson>> localHM) {
        HashMap<String, DiffList> warningList = new HashMap<String, DiffList>();
        ArrayList<String> blackList = new ArrayList<String>();

        ArrayList<ElementInJson> elementsInRequestJson;
        ArrayList<ElementInJson> elementsInLocalJson;

        ArrayList<ElementInJson> elementsMiss;
        ArrayList<ElementInJson> elementsMore;
        DiffList diffList;

        /* For each ID hashed from JS / CSS content:
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
                blackList.add(id);
            }
        }

        return new ComparisonResult(warningList, blackList);
    }

    public boolean isEmpty() {
        return this.externalComparisonResult.isEmpty() &&
               this.blockComparisonResult.isEmpty() &&
               this.inlineComparisonResult.isEmpty();
    }

    public void updateLocalJson(HashMapInJson localJson) {
        HashMap<String, DiffList> warningList = inlineComparisonResult.getWarningList();
        for (String id : warningList.keySet()) {
            localJson.getInline().get(id).addAll(warningList.get(id).getMissList());
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

    public ComparisonResult getExternalComparisonResult() {
        return externalComparisonResult;
    }

    public void setExternalComparisonResult(ComparisonResult externalComparisonResult) {
        this.externalComparisonResult = externalComparisonResult;
    }

    public ComparisonResult getBlockComparisonResult() {
        return blockComparisonResult;
    }

    public void setBlockComparisonResult(ComparisonResult blockComparisonResult) {
        this.blockComparisonResult = blockComparisonResult;
    }

    public ComparisonResult getInlineComparisonResult() {
        return inlineComparisonResult;
    }

    public void setInlineComparisonResult(ComparisonResult inlineComparisonResult) {
        this.inlineComparisonResult = inlineComparisonResult;
    }
}
