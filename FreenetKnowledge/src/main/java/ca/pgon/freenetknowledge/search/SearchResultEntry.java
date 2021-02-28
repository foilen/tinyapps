/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.search;

import java.util.ArrayList;
import java.util.List;

import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

public class SearchResultEntry {
    public UrlEntity urlEntity;
    public List<String> description = new ArrayList<String>();

    public SearchResultEntry() {
    }
}
