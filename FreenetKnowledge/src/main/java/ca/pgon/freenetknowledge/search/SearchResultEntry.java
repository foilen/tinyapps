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
