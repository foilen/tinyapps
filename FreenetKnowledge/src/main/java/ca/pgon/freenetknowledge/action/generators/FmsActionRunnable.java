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
package ca.pgon.freenetknowledge.action.generators;

import java.util.logging.Level;
import java.util.logging.Logger;

import ca.pgon.freenetknowledge.repository.dao.UrlDao;
import ca.pgon.freenetknowledge.repository.manager.UrlManager;
import ca.pgon.freenetknowledge.search.SearchEngine;
import ca.pgon.freenetknowledge.spiders.news.NewsCrawler;
import ca.pgon.freenetknowledge.spiders.news.NewsUtils;
import ca.pgon.freenetknowledge.utils.LinksUtils;
import ca.pgon.freenetknowledge.utils.URLUtils;

/**
 * This class is checking all of FMS forums and get the new links it can from it.
 */
public class FmsActionRunnable implements Runnable {
    static private final Logger logger = Logger.getLogger(FmsActionRunnable.class.getName());

    private UrlDao urlDAO;
    private URLUtils urlUtils;
    private LinksUtils linksUtils;
    private UrlManager urlManager;
    private NewsUtils newsUtils;
    private SearchEngine searchEngine;

    /**
     * This method will check all the FMS forums and get all its links.
     */
    @Override
    public void run() {
        logger.info("Starting FMS scan");

        NewsCrawler newsCrawler = new NewsCrawler();

        newsCrawler.setUrlDAO(urlDAO);
        newsCrawler.setUrlUtils(urlUtils);
        newsCrawler.setLinksUtils(linksUtils);
        newsCrawler.setUrlManager(urlManager);
        newsCrawler.setNewsUtils(newsUtils);
        newsCrawler.setSearchEngine(searchEngine);

        try {
            newsCrawler.run();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Problem with the FMS application", e);
        }

        logger.info("End of FMS scan");
    }

    /**
     * @return the urlDAO
     */
    public UrlDao getUrlDAO() {
        return urlDAO;
    }

    /**
     * @param urlDAO
     *            the urlDAO to set
     */
    public void setUrlDAO(UrlDao urlDAO) {
        this.urlDAO = urlDAO;
    }

    /**
     * @return the urlUtils
     */
    public URLUtils getUrlUtils() {
        return urlUtils;
    }

    /**
     * @param urlUtils
     *            the urlUtils to set
     */
    public void setUrlUtils(URLUtils urlUtils) {
        this.urlUtils = urlUtils;
    }

    /**
     * @return the linksUtils
     */
    public LinksUtils getLinksUtils() {
        return linksUtils;
    }

    /**
     * @param linksUtils
     *            the linksUtils to set
     */
    public void setLinksUtils(LinksUtils linksUtils) {
        this.linksUtils = linksUtils;
    }

    /**
     * @return the urlManager
     */
    public UrlManager getUrlManager() {
        return urlManager;
    }

    /**
     * @param urlManager
     *            the urlManager to set
     */
    public void setUrlManager(UrlManager urlManager) {
        this.urlManager = urlManager;
    }

    /**
     * @return the newsUtils
     */
    public NewsUtils getNewsUtils() {
        return newsUtils;
    }

    /**
     * @param newsUtils
     *            the newsUtils to set
     */
    public void setNewsUtils(NewsUtils newsUtils) {
        this.newsUtils = newsUtils;
    }

    /**
     * @return the searchEngine
     */
    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    /**
     * @param searchEngine
     *            the searchEngine to set
     */
    public void setSearchEngine(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

}
