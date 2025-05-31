package ca.pgon.freenetknowledge.repository.dao;

import java.util.List;

import ca.pgon.freenetknowledge.repository.entities.SearchUrlEntity;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

/**
 * The DAO for the full url search.
 */
public interface SearchUrlDao {
    /**
     * Create an url.
     *
     * @param ue
     *            the url entity that is stored in the DB
     */
    void createOrUpdateURL(UrlEntity ue);

    /**
     * Retrieve all the urls that looks like the query.
     *
     * @param query
     *            the query
     * @return the list
     */
    List<String> findUrlLike(String query);

    /**
     * Get the url with the id.
     *
     * @param urlId
     *            the id
     * @return the url
     */
    SearchUrlEntity get(Long urlId);
}
