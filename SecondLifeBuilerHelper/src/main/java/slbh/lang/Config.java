/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

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
