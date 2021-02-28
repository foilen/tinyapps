/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.search;

public class SearchTools {
    public static int findWordPosition(String fullDescription, String term) {
        for (String t : term.split(" ")) {
            int pos = fullDescription.indexOf(t);
            if (pos != -1) {
                return pos;
            }
        }

        return -1;
    }

    static public String getPartAround(String full, int position, int maxCaracters) {
        // If same size or smaller than the max
        if (full.length() <= maxCaracters) {
            return full;
        }

        // Common
        int half = maxCaracters / 2;
        int startPos = 0;

        // In the middle or left
        if (position - half > 0) {
            startPos = position - half;
        }

        // In the right part
        if (position + half >= full.length()) {
            startPos = full.length() - maxCaracters;
        }

        return full.substring(startPos, Math.min(startPos + maxCaracters, full.length()));
    }
}
