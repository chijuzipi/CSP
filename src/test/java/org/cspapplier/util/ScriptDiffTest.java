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

    private ArrayList<String> simpleCases;
    private ScriptDiff scriptDiff;

    @Before
    public void setUp() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("aaabbccc");
        list.add("aaadfssfdsdccc");
        scriptDiff = new ScriptDiff(list);
    }

    @Test
    public void testTwoStringDiff() throws Exception {
        String overlap = scriptDiff.TwoStringDiff("aaabbccc", "aaadfdsfsddccc");
        assertEquals("aaaccc", overlap);
    }
}