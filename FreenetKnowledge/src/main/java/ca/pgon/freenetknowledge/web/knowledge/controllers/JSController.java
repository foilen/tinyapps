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