/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014

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

import java.util.ArrayList;
import java.util.List;

import ca.pgon.freenetknowledge.repository.entities.UrlEntity;
import ca.pgon.freenetknowledge.search.SearchEngine;
import ca.pgon.freenetknowledge.search.SearchResultEntry;

/**
 * This dummy class will not do anything to save data.
 * 
 * @author Simon Levesque
 * 
 */
public class DummySearchEngine implements SearchEngine {

	@Override
	public void addDescription(UrlEntity forURL, UrlEntity refererURL, String content) {

	}

	@Override
	public void removeAllDescriptionsFromReferer(UrlEntity refererURL) {

	}

	@Override
	public List<SearchResultEntry> searchTerm(String term) {
		return new ArrayList<SearchResultEntry>();
	}

}
