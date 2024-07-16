/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.action.generators;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import ca.pgon.freenetknowledge.repository.dao.UrlDao;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;
import ca.pgon.freenetknowledge.repository.manager.UrlManager;
import ca.pgon.freenetknowledge.repository.manager.exception.RetryException;
import ca.pgon.freenetknowledge.utils.LinksUtils;
import ca.pgon.freenetknowledge.utils.URLUtils;

/**
 * This class is getting the document associated with the URL and get the new links it can from it.
 */
public class CrawlingActionRunnable implements Runnable {
    static private final Logger logger = Logger.getLogger(CrawlingActionRunnable.class.getName());

    private UrlDao urlDAO;
    private URLUtils urlUtils;
    private LinksUtils linksUtils;
    private UrlManager urlManager;

    private UrlEntity urlEntity;

    /**
     * The constructor.
     *
     * @param urlEntity
     *            the url to visit
     */
    public CrawlingActionRunnable(UrlEntity urlEntity) {
        this.urlEntity = urlEntity;
    }

    /**
     * Add a new URL to the Database.
     *
     * @param url
     *            the url
     * @throws Exception
     *             an exception
     */
    private void AddURL(UrlEntity url) {
        boolean retry = true;

        while (retry) {
            retry = false;

            try {
                // Get a similar URL in the DB
                UrlEntity existingURL = urlDAO.findSimilar(url.getType(), url.getHash(), url.getName(), url.getPath());

                // If there is one, merge it with this one or insert in the DB.
                if (existingURL == null) {
                    urlManager.createURL(url);
                } else {
                    urlUtils.merge(url, existingURL);
                    urlDAO.updateURLignoringVersionning(existingURL);
                }

                switch (url.getType()) {
                case SSK:
                case USK:
                    // Add also a link of the base site.
                    if (StringUtils.isNotEmpty(url.getPath())) {
                        UrlEntity rootUrl = urlUtils.cloneURL(url);
                        rootUrl.setPath("");
                        rootUrl.setLast_visited(null);
                        rootUrl.setError(false);
                        rootUrl.setVisited(false);
                        AddURL(rootUrl);
                    }
                default: // Nothing to add
                    break;
                }
            } catch (RetryException e) {
                retry = true;
            }
        }
    }

    /**
     * @return the linksUtils
     */
    public LinksUtils getLinksUtils() {
        return linksUtils;
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
     * This method will visit the URL and get all its links.
     */
    @Override
    public void run() {
        logger.log(Level.INFO, "Looking at {0}", urlUtils.toURL(urlEntity));

        try {
            // Visit and get the urls
            if (urlUtils.isVisitable(urlEntity)) {
                List<UrlEntity> newLinks = linksUtils.getLinks(urlEntity);

                for (UrlEntity u : newLinks) {
                    AddURL(u);
                }
            }
            // Update the entry in the DB
            urlEntity.setLast_visited(new Date());
            urlEntity.setVisiting(false);
            urlEntity.setVisited(true);
            urlDAO.updateURLignoringVersionning(urlEntity);
        } catch (Exception e) {
            if (e.getMessage().equals("Connection refused: connect")) {
                // Issue connecting with Freenet
                urlEntity.setLast_visited(null);
                urlDAO.updateURLignoringVersionning(urlEntity);
                logger.severe("Cannot connect to Freenet");
            } else if (e.getMessage().startsWith("Server returned HTTP response code: 500")) {
                // The url is not available
                urlEntity.setError(true);
                urlDAO.updateURLignoringVersionning(urlEntity);
            } else {
                // Unknown error
                urlEntity.setError(true);
                urlDAO.updateURLignoringVersionning(urlEntity);
                logger.log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * @param linksUtils
     *            the linksUtils to set
     */
    public void setLinksUtils(LinksUtils linksUtils) {
        this.linksUtils = linksUtils;
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
