/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.search.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;

import ca.pgon.freenetknowledge.repository.dao.UrlDao;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;
import ca.pgon.freenetknowledge.search.SearchEngine;
import ca.pgon.freenetknowledge.search.SearchResultEntry;
import ca.pgon.freenetknowledge.search.SearchTools;

public class LuceneSearchEngine implements SearchEngine {
    static private Logger logger = Logger.getLogger(LuceneSearchEngine.class.getName());

    static public final Version LUCENE_VERSION = Version.LUCENE_34;
    static private final int LUCENE_MAX_HITS = 10000;
    static private final int LUCENE_MAX_DESCRIPTION_CARACTERS = 125;

    static public final String INDEX_FOR_URL = "for";
    static public final String INDEX_REFERER_URL = "referer";
    static public final String INDEX_CONTENT = "content";

    private String indexDirectory;
    private int maxAddInIndexBeforeComputing;

    private LuceneIndexerThread luceneIndexerThread;
    private Directory directory;
    private Analyzer analyzer;

    @Autowired
    private UrlDao urlDAO;

    @Override
    public void addDescription(UrlEntity forURL, UrlEntity refererURL, String content) {
        luceneIndexerThread.queueAdding(forURL, refererURL, content);
    }

    @PreDestroy
    public void cleanlyCloseIndexer() {
        luceneIndexerThread.requestStop();
    }

    public String getIndexDirectory() {
        return indexDirectory;
    }

    public int getMaxAddInIndexBeforeComputing() {
        return maxAddInIndexBeforeComputing;
    }

    @PostConstruct
    public void init() throws IOException {
        directory = new SimpleFSDirectory(new File(indexDirectory));
        analyzer = new StandardAnalyzer(LuceneSearchEngine.LUCENE_VERSION);
        luceneIndexerThread = new LuceneIndexerThread(maxAddInIndexBeforeComputing, directory, analyzer);
        luceneIndexerThread.start();
    }

    @Override
    public void removeAllDescriptionsFromReferer(UrlEntity refererURL) {
        luceneIndexerThread.removing(refererURL);
    }

    @Override
    public List<SearchResultEntry> searchTerm(String term) {
        IndexSearcher indexSearcher = null;
        try {
            // Init the needed components
            IndexReader indexReader = IndexReader.open(directory);
            indexSearcher = new IndexSearcher(indexReader);
            QueryParser queryParser = new QueryParser(LUCENE_VERSION, INDEX_CONTENT, analyzer);

            // Create the query
            Query query;
            query = queryParser.parse(term);

            // Get the search result
            TopDocs topDocs = indexSearcher.search(query, LUCENE_MAX_HITS);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            // Change them to urls
            Map<String, SearchResultEntry> alreadyIn = new HashMap<>();
            List<SearchResultEntry> results = new ArrayList<>();
            for (ScoreDoc sd : scoreDocs) {
                // Get the url
                Document document = indexSearcher.doc(sd.doc);
                String urlId = document.get(INDEX_FOR_URL);

                SearchResultEntry sre;
                if (alreadyIn.containsKey(urlId)) {
                    sre = alreadyIn.get(urlId);
                } else {
                    sre = new SearchResultEntry();
                    UrlEntity ue = urlDAO.get(Integer.valueOf(urlId));
                    sre.urlEntity = ue;

                    if (ue == null) {
                        continue;
                    }
                    if (ue.isError()) {
                        continue;
                    }

                    alreadyIn.put(urlId, sre);
                    results.add(sre);
                }

                // Add the description
                String fullDescription = document.get(INDEX_CONTENT);
                String partialDescription = SearchTools.getPartAround(fullDescription, SearchTools.findWordPosition(fullDescription, term), LUCENE_MAX_DESCRIPTION_CARACTERS);
                partialDescription = partialDescription.replace('\n', ' ');
                partialDescription = partialDescription.replace('\r', ' ');
                if (!sre.description.contains(partialDescription)) {
                    sre.description.add(partialDescription);
                }
            }

            return results;
        } catch (ParseException e) {
            logger.log(Level.SEVERE, "Error while parsing the search term", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while searching", e);
        } finally {
            if (indexSearcher != null) {
                try {
                    indexSearcher.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    public void setIndexDirectory(String indexDirectory) {
        this.indexDirectory = indexDirectory;
    }

    public void setMaxAddInIndexBeforeComputing(int maxAddInIndexBeforeComputing) {
        this.maxAddInIndexBeforeComputing = maxAddInIndexBeforeComputing;
    }
}
