/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2016

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
package slbh.lang;

import java.util.List;
import java.util.Vector;
import java.util.prefs.Preferences;

public class Config {
    static private Language myLanguage;
    static private String lang;
    static private Preferences myPref;
    static private List<Language> allLangs;

    static {
        allLangs = new Vector<Language>();

        allLangs.add(new English());
        allLangs.add(new French());
    }

    static public void load() {
        // Load config
        myPref = Preferences.userRoot().node("/slbh");
        lang = myPref.get("lang", "English");
        change(lang);
    }

    static public void change(String _lang) {
        lang = _lang;

        for (Language l : allLangs) {
            if (l.lang().equals(lang)) {
                myLanguage = l;
                break;
            }
        }

        // Save config
        myPref.put("lang", lang);
    }

    static public Language getMyLanguage() {
        return myLanguage;
    }

    static public String[] getAllLanguages() {
        String[] result = new String[allLangs.size()];

        int pos = 0;
        for (Language l : allLangs) {
            result[pos++] = l.lang();
        }

        return result;
    }
}
