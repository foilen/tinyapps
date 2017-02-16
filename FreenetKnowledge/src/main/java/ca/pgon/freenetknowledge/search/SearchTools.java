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

public class SearchTools {
	public static int findWordPosition(String fullDescription, String term) {
		for (String t : term.split(" ")) {
			int pos = fullDescription.indexOf(t);
			if (pos != -1)
				return pos;
		}

		return -1;
	}

	static public String getPartAround(String full, int position, int maxCaracters) {
		// If same size or smaller than the max
		if (full.length() <= maxCaracters)
			return full;

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
