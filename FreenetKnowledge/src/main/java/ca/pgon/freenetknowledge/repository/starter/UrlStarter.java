/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.repository.starter;

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import ca.pgon.freenetknowledge.repository.dao.UrlDao;
import ca.pgon.freenetknowledge.repository.manager.UrlManager;
import ca.pgon.freenetknowledge.repository.manager.exception.RetryException;

/**
 * This class cleans the Url table at startup and add some default urls if none are presents.
 */
public class UrlStarter implements Starter {

    static private final Logger logger = Logger.getLogger(UrlStarter.class.getName());

    @Autowired
    private UrlDao urlDao;

    @Autowired
    private UrlManager urlManager;

    private String[] defaultUrls;

    /**
     * @return the defaultUrls
     */
    public String[] getDefaultUrls() {
        return defaultUrls;
    }

    /**
     * @param defaultUrls
     *            the defaultUrls to set
     */
    public void setDefaultUrls(String[] defaultUrls) {
        this.defaultUrls = defaultUrls;
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    @Override
    public void startup() {
        // Reset the currently visiting urls
        logger.info("Reseting the currently visiting urls");
        urlDao.resetVisiting();

        // Check how many urls there are
        long count = urlDao.getCount();
        logger.log(Level.INFO, "There are currently {0} urls in the DB", count);

        // If none, create some
        if (count == 0) {
            logger.info("Since there are no urls in the DB, creating some");

            if (defaultUrls != null) {
                for (String url : defaultUrls) {
                    try {
                        urlManager.createURL(url);
                    } catch (RetryException e) {
                        // No retry for this since there are none
                    }
                }
            }

            // Display the amount now
            count = urlDao.getCount();
            logger.log(Level.INFO, "There are currently {0} urls in the DB", count);
        }
    }

}
