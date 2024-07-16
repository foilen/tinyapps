/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

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
     * @return the linksUtils
     */
    public LinksUtils getLinksUtils() {
        return linksUtils;
    }

    /**
     * @return the newsUtils
     */
    public NewsUtils getNewsUtils() {
        return newsUtils;
    }

    /**
     * @return the searchEngine
     */
    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    /**
     * @return the urlDAO
     */
    public UrlDao getUrlDAO() {
        return urlDAO;
    }

    /**
     * @return the urlManager
     */
    public UrlManager getUrlManager() {
        return urlManager;
    }

    /**
     * @return the urlUtils
     */
    public URLUtils getUrlUtils() {
        return urlUtils;
    }

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
     * @param linksUtils
     *            the linksUtils to set
     */
    public void setLinksUtils(LinksUtils linksUtils) {
        this.linksUtils = linksUtils;
    }

    /**
     * @param newsUtils
     *            the newsUtils to set
     */
    public void setNewsUtils(NewsUtils newsUtils) {
        this.newsUtils = newsUtils;
    }

    /**
     * @param searchEngine
     *            the searchEngine to set
     */
    public void setSearchEngine(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    /**
     * @param urlDAO
     *            the urlDAO to set
     */
    public void setUrlDAO(UrlDao urlDAO) {
        this.urlDAO = urlDAO;
    }

    /**
     * @param urlManager
     *            the urlManager to set
     */
    public void setUrlManager(UrlManager urlManager) {
        this.urlManager = urlManager;
    }

    /**
     * @param urlUtils
     *            the urlUtils to set
     */
    public void setUrlUtils(URLUtils urlUtils) {
        this.urlUtils = urlUtils;
    }

}
