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
package ca.pgon.freenetknowledge.repository.manager;

import ca.pgon.freenetknowledge.repository.entities.UrlEntity;
import ca.pgon.freenetknowledge.repository.manager.exception.RetryException;

/**
 * To manage the url in the multiple DAOs.
 */
public interface UrlManager {
    /**
     * Parse a url and create a URLEntity.
     * 
     * @param url
     *            the url
     * @return the created URLEntity
     * @throws RetryException
     *             ask to retry since it already exists
     */
    UrlEntity createURL(String url) throws RetryException;

    /**
     * Persist the given URLEntity.
     * 
     * @param ue
     *            the URLEntity
     * @throws RetryException
     *             ask to retry since it already exists
     */
    void createURL(UrlEntity ue) throws RetryException;

    // TODO Future - Add a delete
}
