/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2017

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
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
