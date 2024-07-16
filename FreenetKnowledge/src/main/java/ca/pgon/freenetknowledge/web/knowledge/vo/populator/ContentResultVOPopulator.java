/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

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
