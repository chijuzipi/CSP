package test.java.org.cspapplier.util;

import org.junit.Test;
import main.java.org.cspapplier.util.*;

import static org.junit.Assert.assertEquals;

public class SHAHashTest {

    @Test
    public void testGetHashCode() throws Exception {
        String js = "document.getElementById(\"demo\").innerHTML = \"Paragraph changed.\";";
        String result = SHAHash.getHashCode(js);
        String refResult = "e293abf1436314d08f60d713d4a5959aefd6f84c";

        assertEquals(refResult, result);
    }
}