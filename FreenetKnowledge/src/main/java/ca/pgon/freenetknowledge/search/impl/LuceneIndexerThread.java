/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2016

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
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
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
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

	public void queueAdding(UrlEntity forURL, UrlEntity refererURL, String content) {
		queue.add(new Entry(forURL, refererURL, content));
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

				IndexWriterConfig indexWriterConfig = new IndexWriterConfig(LuceneSearchEngine.LUCENE_VERSION, analyzer);
				indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
				indexWriter = new IndexWriter(directory, indexWriterConfig);

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

	public void requestStop() {
		stop.set(true);

		try {
			this.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void removing(UrlEntity refererURL) {
		try {
			semaphore.acquire();

			IndexReader indexReader = IndexReader.open(directory, false);
			Term term = new Term(LuceneSearchEngine.INDEX_REFERER_URL, String.valueOf(refererURL.getId()));

			indexReader.deleteDocuments(term);

			indexReader.close();
		} catch (NoSuchDirectoryException e) {
			// The index is empty, so not an issue
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while removing referer", e);
		} finally {
			semaphore.release();
		}
	}
}
