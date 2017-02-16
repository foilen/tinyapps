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
package ca.pgon.freenetknowledge.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.pgon.freenetknowledge.repository.entities.UrlEntity;
import ca.pgon.freenetknowledge.search.SearchEngine;

@Component
public class LinksUtils {
    static private final Logger logger = Logger.getLogger(LinksUtils.class.getName());
    static private final int GET_TIMEOUT = 300000;

    @Autowired
    private SearchEngine searchEngine;

    @Autowired
    private URLUtils urlUtils;

    /**
     * Get an absolute url.
     * 
     * @param ue
     *            the URLEntity on which the url was found
     * @param url
     *            the relative or absolute url
     * @return an absolute url
     */
    public String forgeUrl(UrlEntity ue, String url) {
        if (url.indexOf('#') != -1) {
            url = url.substring(0, url.indexOf('#'));
        }

        if (url.indexOf("&amp") != -1) {
            url = url.substring(0, url.indexOf("&amp"));
        }

        if (url.startsWith("/")) {
            // Absolute
        } else {
            // Relative
            UrlEntity newUrl = urlUtils.cloneURL(ue);

            int pos;
            if ((StringUtils.isEmpty(ue.getPath())) || ((pos = newUrl.getPath().lastIndexOf('/')) == -1)) {
                // Check if it is a file or a folder
                if (StringUtils.isNotEmpty(ue.getPath()) && newUrl.getPath().indexOf('.') == -1) {
                    newUrl.setPath(newUrl.getPath() + "/" + url);
                } else {
                    newUrl.setPath(url);
                }
            } else {
                newUrl.setPath(newUrl.getPath().substring(0, pos) + "/" + url);
            }

            url = urlUtils.toURL(newUrl);
        }

        return url;
    }

    public String getDangerousFileType(String text) {
        String search = "Unknown and potentially dangerous content type: ";
        int pos = text.indexOf(search);

        if (pos == -1)
            return null;

        pos += search.length();

        return text.substring(pos, text.indexOf('<', pos));
    }

    public String getDocument(String url) throws IOException {
        logger.fine("URLEntity: getDocumentNormal");
        StringBuffer result = new StringBuffer();

        URLConnection conn = (new URL(url)).openConnection();
        conn.setConnectTimeout(GET_TIMEOUT);
        conn.setReadTimeout(GET_TIMEOUT);
        InputStreamReader file = new InputStreamReader(conn.getInputStream());

        int count;
        char[] buffer = new char[2048];
        while ((count = file.read(buffer)) != -1) {
            for (int i = 0; i < count; ++i) {
                result.append(buffer[i]);
            }
        }

        return result.toString();
    }

    public String getDocument(UrlEntity ue) throws Exception {
        logger.fine("URLEntity: getDocument");

        String result = getDocument(urlUtils.toURL(ue));

        String danger = getDangerousFileType(result);
        if (danger != null) {
            result = getDocument(urlUtils.toURL(ue) + "?type=text/plain");
            danger = getDangerousFileType(result);
        }
        if (danger != null)
            throw new Exception("Unhandled dangerous file type: " + danger);

        return result;
    }

    public List<UrlEntity> getLinks(UrlEntity ue) throws Exception {
        logger.fine("URLEntity: getLinks");
        Vector<UrlEntity> result = new Vector<UrlEntity>();

        // Remove all the old descriptions from this link
        searchEngine.removeAllDescriptionsFromReferer(ue);

        String html = getDocument(ue);

        // Index full html page
        searchEngine.addDescription(ue, ue, html);

        result.addAll(getLinksFromA(ue, html));
        result.addAll(getLinksFromIMG(ue, html));
        result.addAll(getLinksFromThawFile(ue, html));
        result.addAll(getLinksFromThawLink(ue, html));

        return result;
    }

    public List<UrlEntity> getLinksFromA(UrlEntity ue, String html) {
        logger.fine("URLEntity: getLinksFromA");
        Vector<UrlEntity> result = new Vector<UrlEntity>();

        String lowerHtml = html.toLowerCase();

        int pos = 0;
        while ((pos = lowerHtml.indexOf("<a ", pos)) != -1) {
            // Get the full link
            int endPos = lowerHtml.indexOf("</a>", pos);
            if (endPos == -1) {
                endPos = lowerHtml.indexOf('<', pos + 1);
            }
            if (endPos == -1)
                continue;

            String htmlLink = html.substring(pos, endPos);
            String lowerHtmlLink = htmlLink.toLowerCase();

            pos = endPos;

            // Find the link
            int lPos = lowerHtmlLink.indexOf("href=");
            if (lPos == -1)
                continue;

            char opening = lowerHtmlLink.charAt(lPos + 5);

            lPos += 6;

            String linkUrl = htmlLink.substring(lPos, lowerHtmlLink.indexOf(opening, lPos));
            linkUrl = forgeUrl(ue, linkUrl);

            // Find the description
            lPos = lowerHtmlLink.indexOf('>');
            String linkDescription = htmlLink.substring(lPos + 1);

            // Create the link
            try {
                UrlEntity fnLink = urlUtils.parse(linkUrl);
                searchEngine.addDescription(fnLink, ue, linkDescription);
                result.add(fnLink);
            } catch (Exception e) {
                logger.fine("Skipping: " + linkUrl);
            }
        }

        return result;
    }

    public List<UrlEntity> getLinksFromIMG(UrlEntity ue, String html) {
        logger.fine("URLEntity: getLinksFromIMG");
        Vector<UrlEntity> result = new Vector<UrlEntity>();

        String lowerHtml = html.toLowerCase();

        int pos = 0;
        while ((pos = lowerHtml.indexOf("<img ", pos)) != -1) {
            // Get the full link
            int endPos = lowerHtml.indexOf(">", pos);
            if (endPos == -1) {
                ++pos;
                continue;
            }

            String htmlLink = html.substring(pos, endPos);
            String lowerHtmlLink = htmlLink.toLowerCase();

            pos = endPos;

            // Find the link
            int lPos = lowerHtmlLink.indexOf("src=");
            if (lPos == -1)
                continue;

            char opening = lowerHtmlLink.charAt(lPos + 4);

            lPos += 5;

            String linkUrl = htmlLink.substring(lPos, lowerHtmlLink.indexOf(opening, lPos));
            linkUrl = forgeUrl(ue, linkUrl);

            // Find the description
            lPos = lowerHtmlLink.indexOf("alt=");
            String linkDescription = null;
            if (lPos != -1) {
                opening = lowerHtmlLink.charAt(lPos + 4);

                lPos += 5;
                linkDescription = htmlLink.substring(lPos, lowerHtmlLink.indexOf(opening, lPos));
            }

            // Create the link
            try {
                UrlEntity fnLink = urlUtils.parse(linkUrl);
                if (linkDescription != null) {
                    searchEngine.addDescription(fnLink, ue, linkDescription);
                }

                result.add(fnLink);
            } catch (Exception e) {
                logger.fine("Skipping: " + linkUrl);
            }
        }

        return result;
    }

    public List<UrlEntity> getLinksFromText(String text) {
        // TODO Future - Check links on 2 lines
        logger.fine("URLEntity: getLinksFromText");
        Vector<UrlEntity> result = new Vector<UrlEntity>();
        String[] search = { "CHK@", "SSK@", "USK@", "KSK@" };

        for (String currentSearch : search) {
            int pos = 0;
            while ((pos = text.indexOf(currentSearch, pos)) != -1) {
                // Get the full link
                int endPos = text.indexOf("\n", pos);
                if (endPos == -1) {
                    endPos = text.length();
                }

                String linkUrl = text.substring(pos, endPos);

                pos = endPos;

                // Create the link
                try {
                    UrlEntity fnLink = urlUtils.parse(linkUrl);

                    result.add(fnLink);
                } catch (Exception e) {
                    logger.fine("Skipping: " + linkUrl);
                }
            }
        }

        return result;
    }

    public List<UrlEntity> getLinksFromThawFile(UrlEntity ue, String html) {
        logger.fine("URLEntity: getLinksFromThaw");
        Vector<UrlEntity> result = new Vector<UrlEntity>();

        String lowerHtml = html.toLowerCase();

        int pos = 0;
        String linkDescription = "";

        pos = lowerHtml.indexOf("<title>");
        if (pos != -1) {
            pos += 7;
            linkDescription += html.substring(pos, html.indexOf('<', pos)) + " ";
        }
        pos = lowerHtml.indexOf("<category>");
        if (pos != -1) {
            pos += 10;
            linkDescription += html.substring(pos, html.indexOf('<', pos)) + " ";
        }

        while ((pos = lowerHtml.indexOf("<file ", pos)) != -1) {
            // Get the full link
            int endPos = lowerHtml.indexOf('>', pos);
            if (endPos == -1)
                continue;

            String htmlLink = html.substring(pos, endPos);
            String lowerHtmlLink = htmlLink.toLowerCase();

            pos = endPos;

            // Find the link
            int lPos = lowerHtmlLink.indexOf("key=\"");
            if (lPos == -1)
                continue;

            lPos += 5;

            String linkUrl = htmlLink.substring(lPos, lowerHtmlLink.indexOf('"', lPos));

            // Find the size
            lPos = lowerHtmlLink.indexOf("size=\"");
            if (lPos == -1)
                continue;

            lPos += 6;

            String linkSize = htmlLink.substring(lPos, lowerHtmlLink.indexOf('"', lPos));
            if (linkSize.equals("0"))
                linkSize = null;

            // Create the link
            try {
                UrlEntity fnLink = urlUtils.parse(linkUrl);
                fnLink.setSize(linkSize);
                searchEngine.addDescription(fnLink, ue, linkDescription);

                result.add(fnLink);
            } catch (Exception e) {
                logger.fine("Skipping: " + linkUrl);
            }
        }

        return result;
    }

    public List<UrlEntity> getLinksFromThawLink(UrlEntity ue, String html) {
        logger.fine("URLEntity: getLinksFromThaw");
        Vector<UrlEntity> result = new Vector<UrlEntity>();

        String lowerHtml = html.toLowerCase();

        int pos = 0;
        String linkDescription = "";

        pos = lowerHtml.indexOf("<title>");
        if (pos != -1) {
            pos += 7;
            linkDescription += html.substring(pos, html.indexOf('<', pos)) + " ";
        }
        pos = lowerHtml.indexOf("<category>");
        if (pos != -1) {
            pos += 10;
            linkDescription += html.substring(pos, html.indexOf('<', pos)) + " ";
        }

        while ((pos = lowerHtml.indexOf("<link ", pos)) != -1) {
            // Get the full link
            int endPos = lowerHtml.indexOf('>', pos);
            if (endPos == -1)
                continue;

            String htmlLink = html.substring(pos, endPos);
            String lowerHtmlLink = htmlLink.toLowerCase();

            pos = endPos;

            // Find the link
            int lPos = lowerHtmlLink.indexOf("key=\"");
            if (lPos == -1)
                continue;

            lPos += 5;

            String linkUrl = htmlLink.substring(lPos, lowerHtmlLink.indexOf('"', lPos));

            // Create the link
            try {
                UrlEntity fnLink = urlUtils.parse(linkUrl);
                searchEngine.addDescription(fnLink, ue, linkDescription);

                result.add(fnLink);
            } catch (Exception e) {
                logger.fine("Skipping: " + linkUrl);
            }
        }

        return result;
    }
}
