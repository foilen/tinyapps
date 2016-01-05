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
package ca.pgon.freenetknowledge.web.knowledge.vo.populator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.pgon.freenetknowledge.search.SearchResultEntry;
import ca.pgon.freenetknowledge.utils.URLUtils;
import ca.pgon.freenetknowledge.web.knowledge.vo.ContentResultVO;

/**
 * The populator for the content search result.
 */
@Component
public class ContentResultVOPopulator {

    @Autowired
    private URLUtils urlUtils;

    /**
     * Populate a list of content result.
     * 
     * @param searchResultEntries
     * @return the list of results
     */
    public List<ContentResultVO> populate(List<SearchResultEntry> searchResultEntries) {

        List<ContentResultVO> result = new ArrayList<ContentResultVO>();

        for (SearchResultEntry searchResultEntry : searchResultEntries) {
            ContentResultVO contentResultVO = new ContentResultVO();
            contentResultVO.setLink(urlUtils.toURL(searchResultEntry.urlEntity));
            // TODO Future - Not just first description
            contentResultVO.setDescription(searchResultEntry.description.get(0));
            result.add(contentResultVO);
        }

        return result;
    }

}
