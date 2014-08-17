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

import ca.pgon.freenetknowledge.freenet.fnType;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

/**
 * The DAO for the Freenet url.
 * 
 * @author Simon Levesque
 * 
 */
public interface UrlDao {

	/**
	 * Persist the given URLEntity.
	 * 
	 * @param ue
	 *            the URLEntity
	 */
	void createURL(UrlEntity ue);

	void deleteUrl(long urlId);

	UrlEntity findSimilar(fnType type, String hash, String name, String path);

	UrlEntity get(long urlId);

	/**
	 * Tells how many Urls are present in the DB.
	 * 
	 * @return the amount of urls
	 */
	long getCount();

	long getErrorCount();

	UrlEntity getNextToVisit();

	/**
	 * Tells how many Urls were never visited.
	 * 
	 * @return the amount of urls
	 */
	long getToVisitCount();

	long getVisitedCount();

	void resetErrors();

	void resetVisited();

	/**
	 * Set all the currently visiting url to false.
	 */
	void resetVisiting();

	void updateURLignoringVersionning(UrlEntity ue);
}
