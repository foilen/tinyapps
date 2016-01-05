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
package ca.pgon.freenetknowledge.utils;

import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import ca.pgon.freenetknowledge.freenet.fnType;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

@Component
public class URLUtils {
    static private final Logger logger = Logger.getLogger(URLUtils.class.getName());

    static public final String DEFAULT_BASE_URL = "http://127.0.0.1:8888/";

    private String baseURL = DEFAULT_BASE_URL;

    public UrlEntity cloneURL(UrlEntity url) {
        UrlEntity result = new UrlEntity();
        result.copyData(url);
        return result;
    }

    public String getExtension(UrlEntity url) {
        String path = url.getPath();

        if (path != null) {
            int posDot = path.lastIndexOf('.');
            if (posDot == -1)
                return null;
            int posSlash = path.lastIndexOf('/');
            if (posDot > posSlash) {
                return path.substring(posDot + 1).toLowerCase();
            }
        }

        return null;
    }

    private boolean isString(String source, String... cmp) {
        for (String s : cmp) {
            if (source.compareTo(s) == 0)
                return true;
        }

        return false;
    }

    public boolean isVisitable(UrlEntity url) {
        String extension = getExtension(url);
        switch (url.getType()) {
        case CHK:
        case KSK:
            if (extension != null) {
                if (isString(extension, "frdx", "htm", "html"))
                    return true;
            }
            return false;

        case SSK:
        case USK:
            if (extension != null) {
                if (!isString(extension, "frdx", "htm", "html"))
                    return false;
            }
        }

        return true;
    }

    public void merge(UrlEntity source, UrlEntity destination) {
        if (source.getVersion() != null) {
            if (destination.getVersion() == null) { // Had no version
                destination.setVersion(source.getVersion());
                destination.setSize(source.getSize());
                destination.setLast_visited(source.getLast_visited());
            } else if (source.getVersion().compareTo(destination.getVersion()) != 0) {
                // Have different versions
                try {
                    int srcVer = Integer.parseInt(source.getVersion());
                    int dstVer = Integer.parseInt(destination.getVersion());

                    if (srcVer > dstVer) { // Had a newer version
                        destination.setVersion(source.getVersion());
                        destination.setSize(source.getSize());
                        destination.setLast_visited(source.getLast_visited());
                    }
                } catch (Exception e) {
                    // Ignore if there is an error parsing the version
                }
            }
        }

    }

    public UrlEntity parse(String url) throws Exception {
        logger.fine("parse(\"" + url + "\")");

        UrlEntity ue = new UrlEntity();

        // Clean
        url = url.trim();
        url = url.replaceAll(" ", "%20");

        // Remove all useless information
        if (url.contains("<")) {
            throw (new Exception("Contains <"));
        }
        if (url.contains(">")) {
            throw (new Exception("Contains >"));
        }
        int pos = url.indexOf('@');
        if (pos == -1) {
            throw (new Exception("Could not find @"));
        }

        if (pos < 3) {
            throw (new Exception("The @ is at a wrong place"));
        }

        url = url.substring(pos - 3);

        // Get the type
        String newType = url.substring(0, 3);
        url = url.substring(4);

        String[] urlParts = url.split("/");

        ue.setType(fnType.parse(newType));
        if (ue.getType() == null)
            throw (new Exception("The type is wrong"));

        switch (ue.getType()) {
        case CHK:
            ue.setHash(urlParts[0]);
            if (urlParts.length > 1)
                ue.setName(urlParts[1]);
            break;
        case SSK:
            ue.setHash(urlParts[0]);
            ue.setName(urlParts[1].split("-")[0]);
            ue.setVersion(urlParts[1].split("-")[1]);
            if (urlParts.length > 2) {
                ue.setPath(urlParts[2]);
                for (int i = 3; i < urlParts.length; ++i) {
                    ue.setPath(ue.getPath() + "/" + urlParts[i]);
                }
            }
            break;
        case USK:
            ue.setHash(urlParts[0]);
            ue.setName(urlParts[1]);
            ue.setVersion(urlParts[2]);
            if (urlParts.length > 3) {
                ue.setPath(urlParts[3]);
                for (int i = 4; i < urlParts.length; ++i) {
                    ue.setPath(ue.getPath() + "/" + urlParts[i]);
                }
            }
            break;
        case KSK:
            ue.setName(urlParts[0]);
            break;
        }

        return ue;
    }

    /**
     * Get the URL.
     * 
     * @param ue
     *            the UrlEntity
     * @return the url in String
     */
    public String toURLWhitoutBase(UrlEntity ue) {
        String result = ue.getType() + "@";

        switch (ue.getType()) {
        case CHK:
            result += ue.getHash();
            if (StringUtils.isNotEmpty(ue.getName()))
                result += "/" + ue.getName();
            break;
        case SSK:
            result += ue.getHash() + "/";
            result += ue.getName() + "-";
            result += ue.getVersion();
            if (StringUtils.isNotEmpty(ue.getPath()))
                result += "/" + ue.getPath();
            break;
        case USK:
            result += ue.getHash() + "/";
            result += ue.getName() + "/";
            result += ue.getVersion();
            if (StringUtils.isNotEmpty(ue.getPath()))
                result += "/" + ue.getPath();
            break;
        case KSK:
            result += ue.getName();
            break;
        }

        return result;
    }

    /**
     * Get a full absolute URL.
     * 
     * @param ue
     *            the UrlEntity
     * @return the url in String
     */
    public String toURL(UrlEntity ue) {
        String result = baseURL;
        result += toURLWhitoutBase(ue);
        return result;
    }

    /**
     * @return the baseURL
     */
    public String getBaseURL() {
        return baseURL;
    }

    /**
     * @param baseURL
     *            the baseURL to set
     */
    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

}
