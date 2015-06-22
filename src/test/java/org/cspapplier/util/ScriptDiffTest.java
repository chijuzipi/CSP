package org.cspapplier.util;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * ScriptDiffTest
 *
 * Compare the differences of the given strings
 */
public class ScriptDiffTest {

    private ArrayList<String> singleCase;
    private ArrayList<String> simpleCase;
    private ArrayList<String> multiCase1;
    private ArrayList<String> multiCase2;

    @Before
    public void setUp() {

        singleCase = new ArrayList<String>();
        singleCase.add("abcd");

        simpleCase = new ArrayList<String>();
        simpleCase.add("aaabbccc");
        simpleCase.add("aaadfssfdsdccc");

        multiCase1 = new ArrayList<String>();
        multiCase1.add("aaabbccc");
        multiCase1.add("aaadfsfdccc");
        multiCase1.add("aacbbcc");

        multiCase2 = new ArrayList<String>();
        multiCase2.add("{\"attr\": 3, \"token\": 829432, \"test\": \"no\" }");
        multiCase2.add("{\"attr\": 2, \"token\": 422522g, \"test\": \"yes\" }");
        multiCase2.add("{\"attr\": 3, \"token\": g234243, \"test\": \"no\" }");
        multiCase2.add("{\"attr\": 2, \"token\": 43hg334, \"test\": \"no\" }");
        multiCase2.add("{\"attr\": 1, \"token\": gwr32234, \"test\": \"yes\" }");

    }

    @Test
    public void testTwoStringIntersect() throws Exception {
        ScriptDiff scriptDiff = new ScriptDiff(simpleCase);
        String overlap = scriptDiff.TwoStringIntersect(simpleCase.get(0), simpleCase.get(1));
        assertEquals("aaaccc", overlap);
    }

    @Test
    public void testMultiStringIntersect() throws Exception {

        ScriptDiff scriptDiff_edge = new ScriptDiff(singleCase);
        String overlap_edge = scriptDiff_edge.MultiStringIntersect();
        assertEquals("abcd", overlap_edge);

        ScriptDiff scriptDiff1 = new ScriptDiff(multiCase1);
        String overlap1 = scriptDiff1.MultiStringIntersect();
        assertEquals("aaccc", overlap1);

        ScriptDiff scriptDiff2 = new ScriptDiff(multiCase2);
        String overlap2 = scriptDiff2.MultiStringIntersect();
        assertEquals("{\"attr\": , \"token\": , \"test\": \"\" }", overlap2);

        ScriptDiff scriptDiff3 = new ScriptDiff(simpleCase);
        String overlap3 = scriptDiff3.MultiStringIntersect();
        assertEquals("aaaccc", overlap3);
    }

}