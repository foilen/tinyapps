/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.web.knowledge.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ca.pgon.freenetknowledge.repository.dao.SearchUrlDao;
import ca.pgon.freenetknowledge.search.SearchEngine;
import ca.pgon.freenetknowledge.search.SearchResultEntry;
import ca.pgon.freenetknowledge.utils.URLUtils;
import ca.pgon.freenetknowledge.web.knowledge.vo.ContentResultVO;
import ca.pgon.freenetknowledge.web.knowledge.vo.populator.ContentResultVOPopulator;

@Controller
@RequestMapping("/knowledge/search")
public class SearchController {

    @Autowired
    private URLUtils urlUtils;

    @Autowired
    private SearchUrlDao searchUrlDao;

    @Autowired
    private SearchEngine searchEngine;

    @Autowired
    private ContentResultVOPopulator contentResultVOPopulator;

    @RequestMapping("/contentSearch")
    public ModelAndView contentSearch(String contentQuery) {
        ModelAndView modelAndView = new ModelAndView("content_search");

        // Return the query
        modelAndView.addObject("contentQuery", contentQuery);

        // Get the result
        List<ContentResultVO> result = null;
        if (StringUtils.isNotEmpty(contentQuery)) {
            List<SearchResultEntry> searchResult = searchEngine.searchTerm(contentQuery);
            result = contentResultVOPopulator.populate(searchResult);
        }
        modelAndView.addObject("result", result);

        return modelAndView;
    }

    @RequestMapping("/urlSearch")
    public ModelAndView urlSearch(String urlQuery, Boolean endsWith) {
        ModelAndView modelAndView = new ModelAndView("url_search");

        // Return the query
        modelAndView.addObject("urlQuery", urlQuery);
        modelAndView.addObject("endsWith", endsWith);
        if (BooleanUtils.isTrue(endsWith)) {
            urlQuery = "%" + urlQuery;
        } else {
            urlQuery = "%" + urlQuery + "%";
        }

        // Get the result
        List<String> searchResults = searchUrlDao.findUrlLike(urlQuery);

        // Append the base path
        List<String> result = new ArrayList<String>();
        for (String searchResult : searchResults) {
            result.add(urlUtils.getBaseURL() + searchResult);
        }

        modelAndView.addObject("result", result);

        return modelAndView;
    }
}