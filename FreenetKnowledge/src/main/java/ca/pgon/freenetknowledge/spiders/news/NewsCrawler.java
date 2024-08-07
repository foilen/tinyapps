/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.spiders.news;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.dao.DataIntegrityViolationException;

import ca.pgon.freenetknowledge.repository.dao.UrlDao;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;
import ca.pgon.freenetknowledge.repository.manager.UrlManager;
import ca.pgon.freenetknowledge.repository.manager.exception.RetryException;
import ca.pgon.freenetknowledge.search.SearchEngine;
import ca.pgon.freenetknowledge.utils.LinksUtils;
import ca.pgon.freenetknowledge.utils.URLUtils;

public class NewsCrawler extends Thread {
    static private final Logger logger = Logger.getLogger(NewsCrawler.class.getName());

    private boolean running;
    private int totalMessages = 0, visitedMessages = 0;

    private NewsUtils newsUtils;
    private URLUtils urlUtils;
    private UrlDao urlDAO;
    private SearchEngine searchEngine;
    private LinksUtils linksUtils;
    private UrlManager urlManager;

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

    public int getTotalMessages() {
        return totalMessages;
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

    public int getVisitedMessages() {
        return visitedMessages;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        running = true;

        visitedMessages = 0;
        List<String> groups = newsUtils.getAllGroups();
        totalMessages = newsUtils.getTotalMessagesCount();

        // Visit all group
        for (String groupName : groups) {
            // Get all the group's messages
            for (String article : newsUtils.getAllMessagesInGroup(groupName)) {
                ++visitedMessages;

                boolean retry = true;
                while (retry) {
                    retry = false;

                    try {
                        // Get all the links in the article
                        List<UrlEntity> links = linksUtils.getLinksFromText(article);
                        for (UrlEntity l : links) {
                            UrlEntity existingURL = urlDAO.findSimilar(l.getType(), l.getHash(), l.getName(), l.getPath());

                            if (existingURL != null) {
                                urlUtils.merge(l, existingURL);
                                urlDAO.updateURLignoringVersionning(existingURL);
                                l = existingURL;
                            } else {
                                try {
                                    urlManager.createURL(l);
                                } catch (DataIntegrityViolationException e) {
                                    logger.log(Level.WARNING, "Could not create the new url {0}", urlUtils.toURL(l));
                                }
                            }

                            // Add to the index
                            searchEngine.addDescription(l, null, article);
                        }
                    } catch (RetryException e) {
                        retry = true;
                    }
                }
            }
        }

        running = false;
        visitedMessages = totalMessages = 0;
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
