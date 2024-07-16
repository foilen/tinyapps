/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.pgon.freenetknowledge.freenet.fnType;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class URLUtilsTest {
    @Autowired
    private URLUtils urlUtils;

    private void extensionsTesting(UrlEntity url) {
        // Only visit "frdx", "htm", "html"
        url.setPath("/onedir/file.frdx");
        assertTrue(urlUtils.isVisitable(url));

        url.setPath("/onedir/file.htm");
        assertTrue(urlUtils.isVisitable(url));

        url.setPath("/onedir/file.html");
        assertTrue(urlUtils.isVisitable(url));

        // Try other extensions
        url.setPath("/onedir/file.jpg");
        assertFalse(urlUtils.isVisitable(url));

        url.setPath("/onedir/file.txt");
        assertFalse(urlUtils.isVisitable(url));
    }

    private void parseAlwaysNull(UrlEntity result) {
        assertNull(result.getLast_visited());
        assertNull(result.getSize());
    }

    @Test
    public void testCloneURL() {
        UrlEntity expected = new UrlEntity();
        expected.setError(true);
        expected.setHash("hash");
        expected.setLast_visited(new Date());
        expected.setName("name");
        expected.setPath("path");
        expected.setSize("size");
        expected.setType(fnType.KSK);
        expected.setVersion("version");
        expected.setVisiting(true);
        expected.setVisited(false);

        UrlEntity actual = urlUtils.cloneURL(expected);

        assertEquals(expected.getHash(), actual.getHash());
        assertEquals(expected.getLast_visited(), actual.getLast_visited());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getVersion(), actual.getVersion());
        assertEquals(expected.isError(), actual.isError());
        assertEquals(expected.isVisiting(), actual.isVisiting());
        assertEquals(expected.isVisited(), actual.isVisited());
    }

    @Test
    public void testIsVisitable() {
        UrlEntity url = new UrlEntity();

        // CHK and KSK urls are visited only if there is a right extension
        url.setType(fnType.CHK);
        url.setPath("/onedir/");
        assertFalse(urlUtils.isVisitable(url));
        url.setPath("/onedir/file");
        assertFalse(urlUtils.isVisitable(url));
        extensionsTesting(url);

        url.setType(fnType.KSK);
        url.setPath("/onedir/");
        assertFalse(urlUtils.isVisitable(url));
        url.setPath("/onedir/file");
        assertFalse(urlUtils.isVisitable(url));
        extensionsTesting(url);

        // SSK and USK urls are not visited only if the extension is wrong
        url.setType(fnType.SSK);
        url.setPath("/onedir/");
        assertTrue(urlUtils.isVisitable(url));
        url.setPath("/onedir/file");
        assertTrue(urlUtils.isVisitable(url));
        extensionsTesting(url);

        url.setType(fnType.USK);
        url.setPath("/onedir/");
        assertTrue(urlUtils.isVisitable(url));
        url.setPath("/onedir/file");
        assertTrue(urlUtils.isVisitable(url));
        extensionsTesting(url);
    }

    @Test
    public void testMergeVersions() {
        versionTesting("10", "35", "35");
        versionTesting("35", "10", "35");
        versionTesting("-10", "35", "35");
        versionTesting("10", "-35", "10");

        versionTesting(null, "-35", "-35");
        versionTesting("10", null, "10");
    }

    @Test
    public void testMergeVersionsDate() {
        Date date1 = new Date(50000);
        Date date2 = new Date(9000000);

        versionDateTesting("10", "35", date1, date2, date2);
        versionDateTesting("35", "10", date1, date2, date1);
        versionDateTesting("-10", "35", date1, date2, date2);
        versionDateTesting("10", "-35", date1, date2, date1);

        versionDateTesting(null, "-35", null, date2, date2);
        versionDateTesting("10", null, date1, null, date1);
    }

    @Test
    public void testMergeVersionsSize() {
        versionSizeTesting("10", "35", "10", "35", "35");
        versionSizeTesting("35", "10", "35", "10", "35");
        versionSizeTesting("-10", "35", "-10", "35", "35");
        versionSizeTesting("10", "-35", "10", "-35", "10");

        versionSizeTesting(null, "-35", null, "-35", "-35");
        versionSizeTesting("10", null, "10", null, "10");
    }

    @Test
    public void testParse() throws Exception {
        // USK normal
        UrlEntity result = urlUtils.parse("http://127.0.0.1:8888/USK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad/41/");

        assertEquals(fnType.USK, result.getType());
        assertEquals("yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE", result.getHash());
        assertEquals("toad", result.getName());
        assertEquals("41", result.getVersion());
        assertEquals("", result.getPath());
        parseAlwaysNull(result);

        // USK short
        result = urlUtils.parse("http://127.0.0.1:8888/USK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad/41");

        assertEquals(fnType.USK, result.getType());
        assertEquals("yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE", result.getHash());
        assertEquals("toad", result.getName());
        assertEquals("41", result.getVersion());
        assertEquals("", result.getPath());
        parseAlwaysNull(result);

        // USK file
        result = urlUtils.parse("http://127.0.0.1:8888/USK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad/41/activelink.png");

        assertEquals(fnType.USK, result.getType());
        assertEquals("yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE", result.getHash());
        assertEquals("toad", result.getName());
        assertEquals("41", result.getVersion());
        assertEquals("activelink.png", result.getPath());
        parseAlwaysNull(result);

        // USK long path
        result = urlUtils.parse("http://127.0.0.1:8888/USK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad/41/folder/activelink.png");

        assertEquals(fnType.USK, result.getType());
        assertEquals("yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE", result.getHash());
        assertEquals("toad", result.getName());
        assertEquals("41", result.getVersion());
        assertEquals("folder/activelink.png", result.getPath());
        parseAlwaysNull(result);

        // SSK normal
        result = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad-41/");

        assertEquals(fnType.SSK, result.getType());
        assertEquals("yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE", result.getHash());
        assertEquals("toad", result.getName());
        assertEquals("41", result.getVersion());
        assertEquals("", result.getPath());
        parseAlwaysNull(result);

        // SSK short
        result = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad-41");

        assertEquals(fnType.SSK, result.getType());
        assertEquals("yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE", result.getHash());
        assertEquals("toad", result.getName());
        assertEquals("41", result.getVersion());
        assertEquals("", result.getPath());
        parseAlwaysNull(result);

        // SSK file
        result = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad-41/activelink.png");

        assertEquals(fnType.SSK, result.getType());
        assertEquals("yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE", result.getHash());
        assertEquals("toad", result.getName());
        assertEquals("41", result.getVersion());
        assertEquals("activelink.png", result.getPath());
        parseAlwaysNull(result);

        // SSK long path
        result = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad-41/folder/activelink.png");

        assertEquals(fnType.SSK, result.getType());
        assertEquals("yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE", result.getHash());
        assertEquals("toad", result.getName());
        assertEquals("41", result.getVersion());
        assertEquals("folder/activelink.png", result.getPath());
        parseAlwaysNull(result);

        // CHK
        result = urlUtils.parse("http://127.0.0.1:8888/CHK@u5oOFflfB9hxxe2C9ZQKb8i1dSHlJY6b1P0ReEvWkus,H4cVvTp4WAhKAC~Bv-8mDg18h7ngjO0tRHSUR3rXAO8,AAIC--8/Freenet-Knowledge-v0.1.0-0.tar.gz");
        assertEquals(fnType.CHK, result.getType());
        assertEquals("u5oOFflfB9hxxe2C9ZQKb8i1dSHlJY6b1P0ReEvWkus,H4cVvTp4WAhKAC~Bv-8mDg18h7ngjO0tRHSUR3rXAO8,AAIC--8", result.getHash());
        assertEquals("Freenet-Knowledge-v0.1.0-0.tar.gz", result.getName());
        assertEquals("", result.getPath());
        assertEquals("", result.getVersion());
        parseAlwaysNull(result);

        // KSK
        result = urlUtils.parse("http://127.0.0.1:8888/KSK@onefile.txt");
        assertEquals(fnType.KSK, result.getType());
        assertEquals("", result.getHash());
        assertEquals("onefile.txt", result.getName());
        assertEquals("", result.getPath());
        assertEquals("", result.getVersion());
        parseAlwaysNull(result);
    }

    @Test(expected = Exception.class)
    public void testParseWrongUrl1() throws Exception {
        urlUtils.parse("10");
    }

    @Test(expected = Exception.class)
    public void testParseWrongUrl2() throws Exception {
        urlUtils.parse("http://127.0.0.1:8888/KSK@one<file.txt");
    }

    @Test(expected = Exception.class)
    public void testParseWrongUrl3() throws Exception {
        urlUtils.parse("http://127.0.0.1:8888/KSK@one>file.txt");
    }

    @Test(expected = Exception.class)
    public void testParseWrongUrl4() throws Exception {
        urlUtils.parse("KS@onefile.txt");
    }

    @Test(expected = Exception.class)
    public void testParseWrongUrl5() throws Exception {
        urlUtils.parse("KSJ@onefile.txt");
    }

    @Test
    public void testToUrl() throws Exception {
        UrlEntity url;

        url = urlUtils.parse("http://127.0.0.1:8888/USK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad/41/");
        assertEquals("http://127.0.0.1:8888/USK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad/41", urlUtils.toURL(url));

        url = urlUtils.parse("http://127.0.0.1:8888/USK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad/41");
        assertEquals("http://127.0.0.1:8888/USK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad/41", urlUtils.toURL(url));

        url = urlUtils.parse("http://127.0.0.1:8888/USK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad/41/activelink.png");
        assertEquals("http://127.0.0.1:8888/USK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad/41/activelink.png", urlUtils.toURL(url));

        url = urlUtils.parse("http://127.0.0.1:8888/USK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad/41/folder/activelink.png");
        assertEquals("http://127.0.0.1:8888/USK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad/41/folder/activelink.png", urlUtils.toURL(url));

        url = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad-41/");
        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad-41", urlUtils.toURL(url));

        url = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad-41");
        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad-41", urlUtils.toURL(url));

        url = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad-41/activelink.png");
        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad-41/activelink.png", urlUtils.toURL(url));

        url = urlUtils.parse("http://127.0.0.1:8888/SSK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad-41/folder/activelink.png");
        assertEquals("http://127.0.0.1:8888/SSK@yGvITGZzrY1vUZK-4AaYLgcjZ7ysRqNTMfdcO8gS-LY,-ab5bJVD3Lp-LXEQqBAhJpMKrKJ19RnNaZMIkusU79s,AQACAAE/toad-41/folder/activelink.png", urlUtils.toURL(url));

        url = urlUtils.parse("http://127.0.0.1:8888/CHK@u5oOFflfB9hxxe2C9ZQKb8i1dSHlJY6b1P0ReEvWkus,H4cVvTp4WAhKAC~Bv-8mDg18h7ngjO0tRHSUR3rXAO8,AAIC--8/Freenet-Knowledge-v0.1.0-0.tar.gz");
        assertEquals("http://127.0.0.1:8888/CHK@u5oOFflfB9hxxe2C9ZQKb8i1dSHlJY6b1P0ReEvWkus,H4cVvTp4WAhKAC~Bv-8mDg18h7ngjO0tRHSUR3rXAO8,AAIC--8/Freenet-Knowledge-v0.1.0-0.tar.gz",
                urlUtils.toURL(url));

        url = urlUtils.parse("http://127.0.0.1:8888/KSK@onefile.txt");
        assertEquals("http://127.0.0.1:8888/KSK@onefile.txt", urlUtils.toURL(url));

        url = urlUtils.parse("KSK@onefile.txt");
        assertEquals("http://127.0.0.1:8888/KSK@onefile.txt", urlUtils.toURL(url));
    }

    private void versionDateTesting(String srcVer, String dstVer, Date srcDate, Date dstDate, Date expectedDate) {
        UrlEntity source = new UrlEntity(), destination = new UrlEntity();
        source.setVersion(srcVer);
        source.setLast_visited(srcDate);
        destination.setVersion(dstVer);
        destination.setLast_visited(dstDate);
        urlUtils.merge(source, destination);
        assertEquals(expectedDate, destination.getLast_visited());
    }

    private void versionSizeTesting(String srcVer, String dstVer, String srcSize, String dstSize, String expectedSize) {
        UrlEntity source = new UrlEntity(), destination = new UrlEntity();
        source.setVersion(srcVer);
        source.setSize(srcSize);
        destination.setVersion(dstVer);
        destination.setSize(dstSize);
        urlUtils.merge(source, destination);
        assertEquals(expectedSize, destination.getSize());
    }

    private void versionTesting(String srcVer, String dstVer, String expectedVer) {
        UrlEntity source = new UrlEntity(), destination = new UrlEntity();
        source.setVersion(srcVer);
        destination.setVersion(dstVer);
        urlUtils.merge(source, destination);
        assertEquals(expectedVer, destination.getVersion());
    }

}
