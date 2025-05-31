package ca.pgon.freenetknowledge.search;

import java.util.List;

import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

public interface SearchEngine {
    void addDescription(UrlEntity forURL, UrlEntity refererURL, String content);

    void removeAllDescriptionsFromReferer(UrlEntity refererURL);

    List<SearchResultEntry> searchTerm(String term);
}
