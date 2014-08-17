/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014

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
package ca.pgon.linkvalidator;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Given an HTML page, all the links are returned (in absolute).
 */
public class LinksRetriever {

    private Document doc;

    public LinksRetriever(String html, String baseUri) {
        doc = Jsoup.parse(html, baseUri);
    }

    /**
     * Get the links in <a>.
     * 
     * @return the links
     */
    public List<String> getAs() {
        return getLinksIn("a", "abs:href");
    }

    /**
     * Get the links in <embed>.
     * 
     * @return the links
     */
    public List<String> getEmbeds() {
        return getLinksIn("embed", "abs:src");
    }

    /**
     * Get the links in <iframe>.
     * 
     * @return the links
     */
    public List<String> getIframes() {
        return getLinksIn("iframe", "abs:src");
    }

    /**
     * Get the links in <img>.
     * 
     * @return the links
     */
    public List<String> getImages() {
        return getLinksIn("img", "abs:src");
    }

    /**
     * Get the links in <link> (in <head>).
     * 
     * @return the links
     */
    public List<String> getLinks() {
        return getLinksIn("link", "abs:href");
    }

    protected List<String> getLinksIn(String tagName, String attributeName) {
        List<String> results = new ArrayList<>();

        Elements links = doc.getElementsByTag(tagName);
        for (Element link : links) {
            String linkUrl = link.attr(attributeName);
            if (!linkUrl.isEmpty()) {
                results.add(linkUrl);
            }
        }
        return results;
    }

    /**
     * Get the links in <object> (in <param name="movie").
     * 
     * @return the links
     */
    public List<String> getObjects() {
        List<String> results = new ArrayList<>();

        Elements objects = doc.getElementsByTag("object");
        for (Element object : objects) {
            for (Element param : object.getElementsByTag("param")) {
                if (param.attr("name").equalsIgnoreCase("movie")) {
                    String linkUrl = param.attr("abs:value");
                    if (!linkUrl.isEmpty()) {
                        results.add(linkUrl);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Get the links in <script>.
     * 
     * @return the links
     */
    public List<String> getScripts() {
        return getLinksIn("script", "abs:src");
    }

    /**
     * Get the links in <source> (in <video>).
     * 
     * @return the links
     */
    public List<String> getSources() {
        return getLinksIn("source", "abs:src");
    }

}
