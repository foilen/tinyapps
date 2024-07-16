/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.repository.dao;

import ca.pgon.freenetknowledge.freenet.fnType;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

/**
 * The DAO for the Freenet url.
 */
public interface UrlDao {

    /**
     * Persist the given URLEntity.
     *
     * @param ue
     *            the URLEntity
     */
    void createURL(UrlEntity ue);

    void deleteUrl(long urlId);

    UrlEntity findSimilar(fnType type, String hash, String name, String path);

    UrlEntity get(long urlId);

    /**
     * Tells how many Urls are present in the DB.
     *
     * @return the amount of urls
     */
    long getCount();

    long getErrorCount();

    UrlEntity getNextToVisit();

    /**
     * Tells how many Urls were never visited.
     *
     * @return the amount of urls
     */
    long getToVisitCount();

    long getVisitedCount();

    void resetErrors();

    void resetVisited();

    /**
     * Set all the currently visiting url to false.
     */
    void resetVisiting();

    void updateURLignoringVersionning(UrlEntity ue);
}
