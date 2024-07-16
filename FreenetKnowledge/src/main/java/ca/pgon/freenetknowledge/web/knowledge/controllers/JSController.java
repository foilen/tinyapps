/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.web.knowledge.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ca.pgon.freenetknowledge.repository.dao.UrlDao;

@Controller
@RequestMapping("/knowledge/html")
public class JSController {

    @Autowired
    private UrlDao urlDao;

    @RequestMapping("/dbstate")
    public ModelAndView urlSearch(String urlQuery, Boolean endsWith) {
        ModelAndView modelAndView = new ModelAndView("db_state");

        long visitedCount = urlDao.getVisitedCount();
        long totalCount = urlDao.getCount();
        long percentVisited;

        if (totalCount == 0) {
            percentVisited = 100;
        } else {
            percentVisited = (visitedCount * 100 / totalCount);
        }
        modelAndView.addObject("visitedCount", visitedCount);
        modelAndView.addObject("totalCount", totalCount);
        modelAndView.addObject("percentVisited", percentVisited);

        return modelAndView;
    }

}