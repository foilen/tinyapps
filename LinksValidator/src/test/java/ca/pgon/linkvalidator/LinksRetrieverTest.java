/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2017

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
package ca.pgon.linkvalidator;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.pgon.st.light6.ResourceTools;

/**
 * Tests for {@link LinksRetriever}.
 */
public class LinksRetrieverTest {

    private LinksRetriever linksRetrieverSample1;

    @Before
    public void before() {
        linksRetrieverSample1 = new LinksRetriever(ResourceTools.getResourceAsString("LinksRetriever-sample1.html", LinksRetrieverTest.class), "http://www.pgon.ca/category/blog/index.html");
    }

    @Test
    public void testGetAs() {
        List<String> links = linksRetrieverSample1.getAs();
        Assert.assertNotNull(links);
        Assert.assertEquals(4, links.size());
        int i = 0;
        Assert.assertEquals("http://google.com", links.get(i++));
        Assert.assertEquals("http://www.pgon.ca/category/blog/images/test.png", links.get(i++));
        Assert.assertEquals("http://www.pgon.ca/images/test.png", links.get(i++));
        Assert.assertEquals("http://www.pgon.ca/category/images/test.png", links.get(i++));
    }

    @Test
    public void testGetEmbeds() {
        List<String> links = linksRetrieverSample1.getEmbeds();
        Assert.assertNotNull(links);
        Assert.assertEquals(1, links.size());
        int i = 0;
        Assert.assertEquals("http://www.pgon.ca/category/blog/files/embedJarisFLVPlayer.swf", links.get(i++));
    }

    @Test
    public void testGetIframes() {
        List<String> links = linksRetrieverSample1.getIframes();
        Assert.assertNotNull(links);
        Assert.assertEquals(1, links.size());
        int i = 0;
        Assert.assertEquals("http://www.youtube.com/embed/aaa", links.get(i++));
    }

    @Test
    public void testGetImages() {
        List<String> links = linksRetrieverSample1.getImages();
        Assert.assertNotNull(links);
        Assert.assertEquals(1, links.size());
        int i = 0;
        Assert.assertEquals("http://www.pgon.ca/category/blog/Tor-Etre-un-noeud-de-sortie.png", links.get(i++));
    }

    @Test
    public void testGetLinks() {
        List<String> links = linksRetrieverSample1.getLinks();
        Assert.assertNotNull(links);
        Assert.assertEquals(12, links.size());
        int i = 0;
        Assert.assertEquals("http://www.pgon.ca/images/favicon.png", links.get(i++));
        Assert.assertEquals("http://www.pgon.ca/images/favicon.png", links.get(i++));
        Assert.assertEquals("http://www.pgon.ca/css/bootstrap.min.css", links.get(i++));
        Assert.assertEquals("http://www.pgon.ca/external/video-js/video-js.min.css", links.get(i++));
        Assert.assertEquals("http://www.pgon.com/xmlrpc.php", links.get(i++));
        Assert.assertEquals("http://www.pgon.com/category/fr/feed", links.get(i++));
        Assert.assertEquals("http://www.pgon.com/xmlrpc.php?rsd", links.get(i++));
        Assert.assertEquals("http://www.pgon.com/wp-includes/wlwmanifest.xml", links.get(i++));
        Assert.assertEquals("http://www.pgon.com/fr/mailinglist", links.get(i++));
        Assert.assertEquals("http://www.pgon.com/fr/vous-tenir-a-jour-sur-les-nouveautes", links.get(i++));
        Assert.assertEquals("http://www.pgon.com/", links.get(i++));
        Assert.assertEquals("http://www.pgon.com/?p=1294", links.get(i++));
    }

    @Test
    public void testGetObjects() {
        List<String> links = linksRetrieverSample1.getObjects();
        Assert.assertNotNull(links);
        Assert.assertEquals(1, links.size());
        int i = 0;
        Assert.assertEquals("http://www.pgon.ca/category/blog/files/JarisFLVPlayer.swf", links.get(i++));
    }

    @Test
    public void testGetScripts() {
        List<String> links = linksRetrieverSample1.getScripts();
        Assert.assertNotNull(links);
        Assert.assertEquals(3, links.size());
        int i = 0;
        Assert.assertEquals("http://www.pgon.ca/external/video-js/video.js", links.get(i++));
        Assert.assertEquals("http://www.pgon.com/wp-includes/js/jquery/jquery.js?ver=1.10.2", links.get(i++));
        Assert.assertEquals("http://www.pgon.com/wp-includes/js/jquery/jquery-migrate.min.js?ver=1.2.1", links.get(i++));
    }

    @Test
    public void testGetSources() {
        List<String> links = linksRetrieverSample1.getSources();
        Assert.assertNotNull(links);
        Assert.assertEquals(1, links.size());
        int i = 0;
        Assert.assertEquals("https://s3.amazonaws.com/foilen/Tor%20-%20Etre%20un%20noeud%20de%20sortie.mp4", links.get(i++));
    }
}
