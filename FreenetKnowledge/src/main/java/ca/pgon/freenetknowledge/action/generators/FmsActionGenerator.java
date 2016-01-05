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
package ca.pgon.freenetknowledge.action.generators;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.pgon.freenetknowledge.action.ActionGenerator;
import ca.pgon.freenetknowledge.repository.dao.UrlDao;
import ca.pgon.freenetknowledge.repository.manager.UrlManager;
import ca.pgon.freenetknowledge.search.SearchEngine;
import ca.pgon.freenetknowledge.spiders.news.NewsUtils;
import ca.pgon.freenetknowledge.utils.LinksUtils;
import ca.pgon.freenetknowledge.utils.URLUtils;

/**
 * This class creates new fms search actions.
 */
@Component
public class FmsActionGenerator implements ActionGenerator {

    private static long WAITING_DELAY = 60 * 10 * 1000;// 10 minutes

    @Autowired
    private UrlDao urlDAO;
    @Autowired
    private URLUtils urlUtils;
    @Autowired
    private LinksUtils linksUtils;
    @Autowired
    private UrlManager urlManager;
    @Autowired
    private NewsUtils newsUtils;
    @Autowired
    private SearchEngine searchEngine;

    private Date lastGen;

    /**
     * {@inheritDoc}
     */
    @Override
    synchronized public Runnable getOne() {
        Date currentTime = new Date();
        if (lastGen == null || lastGen.getTime() + WAITING_DELAY < currentTime.getTime()) {
            lastGen = currentTime;

            FmsActionRunnable fmsActionRunnable = new FmsActionRunnable();

            fmsActionRunnable.setUrlDAO(urlDAO);
            fmsActionRunnable.setUrlUtils(urlUtils);
            fmsActionRunnable.setLinksUtils(linksUtils);
            fmsActionRunnable.setUrlManager(urlManager);
            fmsActionRunnable.setNewsUtils(newsUtils);
            fmsActionRunnable.setSearchEngine(searchEngine);

            return fmsActionRunnable;
        }

        return null;
    }
}
