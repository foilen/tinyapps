/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.repository.manager;

import ca.pgon.freenetknowledge.repository.entities.UrlEntity;
import ca.pgon.freenetknowledge.repository.manager.exception.RetryException;

/**
 * To manage the url in the multiple DAOs.
 */
public interface UrlManager {
    /**
     * Parse a url and create a URLEntity.
     *
     * @param url
     *            the url
     * @return the created URLEntity
     * @throws RetryException
     *             ask to retry since it already exists
     */
    UrlEntity createURL(String url) throws RetryException;

    /**
     * Persist the given URLEntity.
     *
     * @param ue
     *            the URLEntity
     * @throws RetryException
     *             ask to retry since it already exists
     */
    void createURL(UrlEntity ue) throws RetryException;

    // TODO Future - Add a delete
}
