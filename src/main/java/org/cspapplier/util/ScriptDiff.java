package org.cspapplier.util;

import java.util.LinkedList;
import java.util.ArrayList;

import com.sksamuel.diffpatch.DiffMatchPatch;
import com.sksamuel.diffpatch.DiffMatchPatch.Diff;

/**
 * ScriptDiff
 *
 * Tell the difference between a bunch of texts.
 */
public class ScriptDiff {

    private ArrayList<String> stringArray;

    public ScriptDiff(ArrayList<String> stringArray) {
        this.stringArray = stringArray;
    }

    public String TwoStringIntersect(String s1, String s2) {
        DiffMatchPatch dmp = new DiffMatchPatch();
        dmp.Diff_Timeout = 16;
        LinkedList<Diff> diffs = dmp.diff_main(s1, s2);
        dmp.diff_cleanupSemantic(diffs);

        String overlap = "";
        for (Diff diff : diffs) {
            if (diff.operation.equals(DiffMatchPatch.Operation.EQUAL))
                overlap += diff.text;
        }

        return overlap;
    }

    public String MultiStringIntersect() {
        if (stringArray.size() < 2)
            return stringArray.get(0);
        else {
            String s1 = stringArray.get(0);
            String s2 = stringArray.get(1);
            String overlap = TwoStringIntersect(s1, s2);

            for (int i = 2; i < stringArray.size(); i++)
                overlap = TwoStringIntersect(overlap, stringArray.get(i));

            return overlap;
        }
    }
}
