/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.action.generators;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.pgon.freenetknowledge.action.ActionGenerator;
import ca.pgon.freenetknowledge.repository.dao.UrlDao;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;
import ca.pgon.freenetknowledge.repository.manager.UrlManager;
import ca.pgon.freenetknowledge.utils.LinksUtils;
import ca.pgon.freenetknowledge.utils.URLUtils;

/**
 * This class creates new crawling actions by using the DB.
 */
@Component
public class CrawlingActionGenerator implements ActionGenerator {

    private static final Logger logger = Logger.getLogger(CrawlingActionGenerator.class.getName());

    @Autowired
    private LinksUtils linksUtils;
    @Autowired
    private UrlDao urlDAO;
    @Autowired
    private URLUtils urlUtils;
    @Autowired
    private UrlManager urlManager;

    @Override
    public Runnable getOne() {
        // Get a URL that was never visited
        UrlEntity nextUrl = urlDAO.getNextToVisit();

        if (nextUrl == null) {
            logger.info("There are no more links to visit. Reseting the visited ones");
            urlDAO.resetVisited();
        } else {
            CrawlingActionRunnable crawlingActionRunnable = new CrawlingActionRunnable(nextUrl);
            crawlingActionRunnable.setLinksUtils(linksUtils);
            crawlingActionRunnable.setUrlDAO(urlDAO);
            crawlingActionRunnable.setUrlUtils(urlUtils);
            crawlingActionRunnable.setUrlManager(urlManager);
            return crawlingActionRunnable;
        }

        return null;
    }

}
