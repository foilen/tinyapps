/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.search;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SearchToolsTest {

    private void partTesting(String full, int position, int maxCaracters, String expected) {
        String result = SearchTools.getPartAround(full, position, maxCaracters);
        assertEquals(expected, result);
    }

    @Test
    public void testGetPartAround() {
        String full = "ABCDEFGHIJKLMNO";

        // Full smaller than the max
        for (int i = 26; i > 14; --i) {
            partTesting(full, 0, i, full);
        }

        // Middle
        partTesting(full, 6, 4, "EFGH");
        partTesting(full, 6, 2, "FG");
        partTesting(full, 6, 3, "FGH");

        // Left
        partTesting(full, 1, 4, "ABCD");
        partTesting(full, 1, 5, "ABCDE");

        // Right
        partTesting(full, 14, 4, "LMNO");
        partTesting(full, 14, 5, "KLMNO");
    }

}
