/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.repository.manager.impl;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.pgon.freenetknowledge.repository.dao.SearchUrlDao;
import ca.pgon.freenetknowledge.repository.dao.UrlDao;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;
import ca.pgon.freenetknowledge.repository.manager.UrlManager;
import ca.pgon.freenetknowledge.repository.manager.exception.RetryException;
import ca.pgon.freenetknowledge.utils.URLUtils;

@Component
public class UrlManagerImpl implements UrlManager {

    private static final Logger logger = Logger.getLogger(UrlManagerImpl.class.getName());

    private Semaphore semaphore = new Semaphore(1);

    @Autowired
    private URLUtils urlUtils;
    @Autowired
    private UrlDao urlDao;
    @Autowired
    private SearchUrlDao searchUrlDao;

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public UrlEntity createURL(String url) throws RetryException {
        // Create the UrlEntity
        UrlEntity ue = null;
        try {
            ue = urlUtils.parse(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Put in the DB
        if (ue != null) {
            createURL(ue);
        }

        return ue;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void createURL(UrlEntity ue) throws RetryException {
        try {
            semaphore.acquire();

            if (urlDao.findSimilar(ue.getType(), ue.getHash(), ue.getName(), ue.getPath()) == null) {
                urlDao.createURL(ue);
                try {
                    searchUrlDao.createOrUpdateURL(ue);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "There is a duplicate url and this should not happens", e);
                    urlDao.deleteUrl(ue.getId());
                }
            } else {
                throw new RetryException();
            }
        } catch (InterruptedException e1) {
        } finally {
            semaphore.release();
        }

    }

}
