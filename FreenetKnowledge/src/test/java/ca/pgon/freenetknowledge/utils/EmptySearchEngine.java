/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.utils;

import java.util.List;

import ca.pgon.freenetknowledge.repository.entities.UrlEntity;
import ca.pgon.freenetknowledge.search.SearchEngine;
import ca.pgon.freenetknowledge.search.SearchResultEntry;

public class EmptySearchEngine implements SearchEngine {

    @Override
    public void addDescription(UrlEntity forURL, UrlEntity refererURL, String content) {
    }

    @Override
    public void removeAllDescriptionsFromReferer(UrlEntity refererURL) {
    }

    @Override
    public List<SearchResultEntry> searchTerm(String term) {
        return null;
    }

}
