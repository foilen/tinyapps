/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.search;

import java.util.List;

import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

public interface SearchEngine {
    void addDescription(UrlEntity forURL, UrlEntity refererURL, String content);

    void removeAllDescriptionsFromReferer(UrlEntity refererURL);

    List<SearchResultEntry> searchTerm(String term);
}
