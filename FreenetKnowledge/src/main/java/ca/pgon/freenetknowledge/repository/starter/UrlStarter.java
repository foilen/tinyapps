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
package ca.pgon.freenetknowledge.repository.starter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

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

}
