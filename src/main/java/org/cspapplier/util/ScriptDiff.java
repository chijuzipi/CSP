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

    public String TwoStringDiff(String s1, String s2) {
        DiffMatchPatch dmp = new DiffMatchPatch();
        dmp.Diff_Timeout = 16;
        LinkedList<Diff> diffs = dmp.diff_main(s1, s2);

        String overlap = "";

        for (Diff diff : diffs) {
            if (diff.operation.equals(DiffMatchPatch.Operation.EQUAL))
                overlap += diff.text;
        }

        return overlap;
    }
}
