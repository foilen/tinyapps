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
package ca.pgon.linkvalidator;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import ca.pgon.st.light6.StreamsUtils;

/**
 * Download an URL, retrieve all the links (if HTML) and try to retrieve them.
 */
public class SiteCrawler extends Thread {

    // The visited sites and their status code
    private Map<String, String> visitedSites = new ConcurrentHashMap<>();
    private Queue<String> sitesToVisit = new ConcurrentLinkedQueue<>();

    private String baseUrl;

    /**
     * @param baseUrl
     *            how the url must start with to be crawled; else, only their status code will be kept
     */
    public SiteCrawler(String baseUrl) {
        this.baseUrl = baseUrl;
        sitesToVisit.add(baseUrl);

        start();
    }

    public void addSiteToVisit(String url) {
        sitesToVisit.add(url);
    }

    @Override
    public void run() {
        while (!sitesToVisit.isEmpty()) {
            String siteToVisit = sitesToVisit.poll();
            try {
                visit(siteToVisit);
            } catch (Exception e) {
                addStatus(siteToVisit, e.getMessage());
            }
        }
    }

    private void visit(String url) throws Exception {
        if (url.startsWith(baseUrl)) {
            maybeCrawl(url);
        } else {
            touch(url);
        }
    }

    private void touch(String url) throws Exception {
        // Already checked
        if (visitedSites.containsKey(url)) {
            return;
        }

        // Check the status code and content type
        URL getter = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) getter.openConnection();
        urlConnection.setRequestMethod("HEAD");
        urlConnection.connect();
        int status = urlConnection.getResponseCode();
        addStatus(url, status);
        urlConnection.disconnect();
    }

    private void maybeCrawl(String url) throws Exception {
        // Already crawled
        if (visitedSites.containsKey(url)) {
            return;
        }

        // Check the status code and content type
        URL getter = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) getter.openConnection();
        urlConnection.setRequestMethod("HEAD");
        urlConnection.connect();
        int status = urlConnection.getResponseCode();
        String contentType = urlConnection.getContentType();
        addStatus(url, status);
        urlConnection.disconnect();

        // Check if should crawl
        if (status == 200 && contentType.contains("html")) {
            crawl(url);
        }
    }

    private void addStatus(String url, int status) {
        String textStatus;
        if (status == 200 || status == 301 || status == 302) {
            textStatus = "OK " + status;
        } else {
            textStatus = "ERROR " + status;
        }
        System.out.println(url + " [" + textStatus + "]");
        visitedSites.put(url, textStatus);
    }

    private void addStatus(String url, String status) {
        String textStatus = "ERROR " + status;
        System.out.println(url + " [" + textStatus + "]");
        visitedSites.put(url, textStatus);
    }

    private void crawl(String url) throws Exception {
        String html = StreamsUtils.consumeAsString(new URL(url).openStream());
        LinksRetriever linksRetriever = new LinksRetriever(html, url);

        getAndAddTheLinks("Scripts", linksRetriever.getScripts());
        getAndAddTheLinks("Links", linksRetriever.getLinks());
        getAndAddTheLinks("Images", linksRetriever.getImages());
        getAndAddTheLinks("Sources", linksRetriever.getSources());
        getAndAddTheLinks("A", linksRetriever.getAs());
        getAndAddTheLinks("Iframes", linksRetriever.getIframes());
        getAndAddTheLinks("Objects", linksRetriever.getObjects());
        getAndAddTheLinks("Embeds", linksRetriever.getEmbeds());
    }

    private void getAndAddTheLinks(String type, List<String> links) {
        if (links.isEmpty()) {
            return;
        }
        System.out.println("\t" + type + ":");
        for (String link : links) {
            sitesToVisit.add(link);
            System.out.println("\t\t" + link);
        }
    }

}
