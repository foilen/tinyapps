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
package ca.pgon.freenetknowledge.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class LinksUtilsTest {

    @Autowired
    private LinksUtils linksUtils;
    @Autowired
    private URLUtils urlUtils;

    @Test
    public void testForgeUrlAbsoluteLink() throws Exception {
        UrlEntity ue = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/");
        String url = "/SSK@Kgjgrfuenkgre,AQACAAE/super-41/activelink.png";

        assertEquals("/SSK@Kgjgrfuenkgre,AQACAAE/super-41/activelink.png", linksUtils.forgeUrl(ue, url));
    }

    @Test
    public void testForgeUrlInFolder1() throws Exception {
        UrlEntity ue = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/");
        String url = "activelink.png";

        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/activelink.png", linksUtils.forgeUrl(ue, url));
    }

    @Test
    public void testForgeUrlInFolder2() throws Exception {
        UrlEntity ue = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/index.html");
        String url = "activelink.png";

        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/activelink.png", linksUtils.forgeUrl(ue, url));
    }

    @Test
    public void testForgeUrlRelativeLink1() throws Exception {
        UrlEntity ue = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/");
        String url = "activelink.png";

        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/activelink.png", linksUtils.forgeUrl(ue, url));
    }

    @Test
    public void testForgeUrlRelativeLink2() throws Exception {
        UrlEntity ue = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/index.html");
        String url = "activelink.png";

        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/activelink.png", linksUtils.forgeUrl(ue, url));
    }

    @Test
    public void testForgeUrlWithAmp() throws Exception {
        UrlEntity ue = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/");
        String url = "activelink.png&amp;value=4";

        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/activelink.png", linksUtils.forgeUrl(ue, url));
    }

    @Test
    public void testForgeUrlWithSharp() throws Exception {
        UrlEntity ue = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/");
        String url = "activelink.png#id=6&amp;value=4";

        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/activelink.png", linksUtils.forgeUrl(ue, url));
    }

    @Test
    public void testGetDangerousFileType() {
        assertEquals("application/x-zip-compressed", linksUtils.getDangerousFileType("<h1>Unknown and potentially dangerous content type: application/x-zip-compressed</h1>"));
    }

    @Test
    public void testGetDangerousFileTypeNot() {
        assertNull(linksUtils.getDangerousFileType("Anything"));
    }

    @Test
    public void testGetLinksFromA() throws Exception {
        String html = "<html><body><a href=\"activelink.png\">Activelink</a><a href='go_deeper/page.html'>Deeper</a><a href='/SSK@Kgjgrfuenkgre,AQACAAE/super-41/activelink.png'>Absolute</a></body></html>";
        UrlEntity ue = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/");

        List<UrlEntity> links = linksUtils.getLinksFromA(ue, html);

        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/activelink.png", urlUtils.toURL(links.get(0)));
        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/go_deeper/page.html", urlUtils.toURL(links.get(1)));
        assertEquals("http://127.0.0.1:8888/SSK@Kgjgrfuenkgre,AQACAAE/super-41/activelink.png", urlUtils.toURL(links.get(2)));
        assertEquals(3, links.size());
    }

    @Test
    public void testGetLinksFromIMG() throws Exception {
        String html = "<html><body><img src=\"activelink.png\">Activelink<img src='go_deeper/page.png'>Deeper<img src='/SSK@Kgjgrfuenkgre,AQACAAE/super-41/activelink.png' alt='Description'>Absolute</body></html>";
        UrlEntity ue = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/");

        List<UrlEntity> links = linksUtils.getLinksFromIMG(ue, html);

        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/activelink.png", urlUtils.toURL(links.get(0)));
        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/go_deeper/page.png", urlUtils.toURL(links.get(1)));
        assertEquals("http://127.0.0.1:8888/SSK@Kgjgrfuenkgre,AQACAAE/super-41/activelink.png", urlUtils.toURL(links.get(2)));
        assertEquals(3, links.size());
    }

    @Test
    public void testGetLinksFromText() throws Exception {
        String text = "Check that file SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/activelink.png\nalso this one:SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/go_deeper/page.png\n and don't forget this last one SSK@Kgjgrfuenkgre,AQACAAE/super-41/activelink.png";

        List<UrlEntity> links = linksUtils.getLinksFromText(text);

        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/activelink.png", urlUtils.toURL(links.get(0)));
        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrusU79s,AQACAAE/toad-41/folder/go_deeper/page.png", urlUtils.toURL(links.get(1)));
        assertEquals("http://127.0.0.1:8888/SSK@Kgjgrfuenkgre,AQACAAE/super-41/activelink.png", urlUtils.toURL(links.get(2)));
        assertEquals(3, links.size());
    }

}
