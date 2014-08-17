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
package ca.pgon.freenetknowledge.repository.dao;

import java.util.List;

import ca.pgon.freenetknowledge.repository.entities.SearchUrlEntity;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

/**
 * The DAO for the full url search.
 * 
 * @author Simon Levesque
 * 
 */
public interface SearchUrlDao {
	/**
	 * Create an url.
	 * 
	 * @param ue
	 *            the url entity that is stored in the DB
	 */
	void createOrUpdateURL(UrlEntity ue);

	/**
	 * Get the url with the id.
	 * 
	 * @param urlId
	 *            the id
	 * @return the url
	 */
	SearchUrlEntity get(Long urlId);

	/**
	 * Retrieve all the urls that looks like the query.
	 * 
	 * @param query
	 *            the query
	 * @return the list
	 */
	List<String> findUrlLike(String query);
}
