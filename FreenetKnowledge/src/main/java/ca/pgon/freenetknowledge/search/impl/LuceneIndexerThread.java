/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.search.impl;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NoSuchDirectoryException;

import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

public class LuceneIndexerThread extends Thread {
    public class Entry {
        public UrlEntity forURL;
        public UrlEntity refererURL;
        public String content;

        public Entry(UrlEntity forURL, UrlEntity refererURL, String content) {
            this.forURL = forURL;
            this.refererURL = refererURL;
            this.content = content;
        }
    }

    static private Logger logger = Logger.getLogger(LuceneIndexerThread.class.getName());

    private Queue<Entry> queue = new ConcurrentLinkedQueue<Entry>();
    private AtomicBoolean stop = new AtomicBoolean(false);
    private Semaphore semaphore = new Semaphore(1);

    private int maxAddInIndexBeforeComputing;
    private Directory directory;
    private Analyzer analyzer;

    public LuceneIndexerThread(int maxAddInIndexBeforeComputing, Directory directory, Analyzer analyzer) throws IOException {
        this.maxAddInIndexBeforeComputing = maxAddInIndexBeforeComputing;
        this.directory = directory;
        this.analyzer = analyzer;
    }

    private void addEntry(IndexWriter indexWriter, Entry entry) {
        Field refererField;
        if (entry.refererURL != null) {
            refererField = new Field(LuceneSearchEngine.INDEX_REFERER_URL, String.valueOf(entry.refererURL.getId()), Store.YES, Index.ANALYZED);
        } else {
            refererField = new Field(LuceneSearchEngine.INDEX_REFERER_URL, "null", Store.YES, Index.ANALYZED);
        }
        Field forField = new Field(LuceneSearchEngine.INDEX_FOR_URL, String.valueOf(entry.forURL.getId()), Store.YES, Index.NO);
        Field contentField = new Field(LuceneSearchEngine.INDEX_CONTENT, entry.content, Store.YES, Index.ANALYZED);

        Document document = new Document();
        document.add(refererField);
        document.add(forField);
        document.add(contentField);

        try {
            indexWriter.addDocument(document);
        } catch (CorruptIndexException e) {
            logger.log(Level.SEVERE, "Description index corrupted", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Description index could not be written", e);
        }
    }

    private IndexWriter genIndexWriter() throws CorruptIndexException, LockObtainFailedException, IOException {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(LuceneSearchEngine.LUCENE_VERSION, analyzer);
        indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(directory, indexWriterConfig);
    }

    public void queueAdding(UrlEntity forURL, UrlEntity refererURL, String content) {
        queue.add(new Entry(forURL, refererURL, content));
    }

    public void removing(UrlEntity refererURL) {
        try {
            semaphore.acquire();

            Term term = new Term(LuceneSearchEngine.INDEX_REFERER_URL, String.valueOf(refererURL.getId()));

            IndexWriter indexWriter = genIndexWriter();
            indexWriter.deleteDocuments(term);

            indexWriter.close();
        } catch (NoSuchDirectoryException e) {
            // The index is empty, so not an issue
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while removing referer", e);
        } finally {
            semaphore.release();
        }
    }

    public void requestStop() {
        stop.set(true);

        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        logger.info("LuceneIndexerThread Starting");
        while (!stop.get()) {
            while (queue.isEmpty() && !stop.get()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }

            IndexWriter indexWriter = null;

            try {
                // Open writer
                semaphore.acquire();

                indexWriter = genIndexWriter();

                // Add at most "maxAddInIndexBeforeComputing" entries in the
                // index before closing it
                for (int i = 0; i < maxAddInIndexBeforeComputing && !queue.isEmpty(); ++i) {
                    addEntry(indexWriter, queue.poll());
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Problem writing the index", e);
            } finally {
                // Close writer
                try {
                    indexWriter.close();
                } catch (Exception e) {
                }
                semaphore.release();
            }
        }

        logger.info("LuceneIndexerThread Stoping");
    }
}
