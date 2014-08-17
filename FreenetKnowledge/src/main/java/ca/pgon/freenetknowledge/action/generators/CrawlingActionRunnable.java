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
                }
            } catch (RetryException e) {
                retry = true;
            }
        }
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

}
