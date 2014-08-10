package org.cspapplier.json;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *  ComparisonResult.java
 *
 *  Store the comparison result of the JsonAnalyzer for further processing and report.
 *  - wariningList: a HashMap containing the IDs which exist for both request json and
 *    local json, but have different elements in the HTML.
 *  - blackList: an ArrayList containing the unknwon IDs appearing in the request json.
 *
 */
public class ComparisonResult {
    private HashMap<String, DiffList> warningList;
    private ArrayList<String> blackList;

    public ComparisonResult() {
        this.warningList = new HashMap<String, DiffList>();
        this.blackList = new ArrayList<String>();
    }

    public ComparisonResult(HashMap<String, DiffList> warningList, ArrayList<String> blackList) {
        this.warningList = warningList;
        this.blackList = blackList;
    }

    public HashMap<String, DiffList> getWarningList() {
        return warningList;
    }

    public void setWarningList(HashMap<String, DiffList> warningList) {
        this.warningList = warningList;
    }

    public ArrayList<String> getBlackList() {
        return blackList;
    }

    public void setBlackList(ArrayList<String> blackList) {
        this.blackList = blackList;
    }
}
