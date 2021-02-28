/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.repository.entities;

/**
 * The full url of the URLEntity.
 */
public class SearchUrlEntity {
    // For DB
    private long id;

    // The url
    private String fullUrl;

    /**
     * @return the fullUrl
     */
    public String getFullUrl() {
        return fullUrl;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param fullUrl
     *            the fullUrl to set
     */
    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

}
