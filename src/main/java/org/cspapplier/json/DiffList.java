package main.java.org.cspapplier.json;

import java.util.ArrayList;

/**
 * DiffList.java
 *
 * A simple object wraping up the elements (in ElementInJson) from:
 * - The missing elements of the request json compared to the local one for a specific ID
 * - The extra(new) elements of the request json compared to the local one for a specific ID
 *
 */
public class DiffList {
    private ArrayList<ElementInJson> missList;
    private ArrayList<ElementInJson> moreList;
    private ArrayList<ElementInJson> unionList;

    public DiffList() {
        this.missList = new ArrayList<ElementInJson>();
        this.moreList = new ArrayList<ElementInJson>();
        this.unionList = new ArrayList<ElementInJson>();
    }

    public ArrayList<ElementInJson> getMissList() {
        return missList;
    }

    public void setMissList(ArrayList<ElementInJson> missList) {
        this.missList = missList;
    }

    public ArrayList<ElementInJson> getMoreList() {
        return moreList;
    }

    public void setMoreList(ArrayList<ElementInJson> moreList) {
        this.moreList = moreList;
    }

    public ArrayList<ElementInJson> getUnionList() {
        return unionList;
    }

    public void setUnionList(ArrayList<ElementInJson> unionList) {
        this.unionList = unionList;
    }
}
