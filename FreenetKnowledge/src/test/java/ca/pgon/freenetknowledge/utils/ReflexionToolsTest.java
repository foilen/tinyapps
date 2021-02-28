/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.utils;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class ReflexionToolsTest {

    private class Bean {
        private String myString;
        private Date myDate;
        private String a;

        public String getA() {
            return a;
        }

        public Date getMyDate() {
            return myDate;
        }

        public String getMyString() {
            return myString;
        }

        public void setA(String a) {
            this.a = a;
        }

        public void setMyDate(Date myDate) {
            this.myDate = myDate;
        }

        public void setMyString(String myString) {
            this.myString = myString;
        }

    }

    private final String expectedString = "some text";

    private final Date expectedDate = new Date();

    @Test
    public void testCopyGetToSetAllValues() {
        Bean bean1 = new Bean();
        Bean bean2 = new Bean();

        bean1.setMyString(expectedString);
        bean1.setMyDate(expectedDate);

        ReflexionTools.copyGetToSet(bean1, bean2, "myString", "myDate");

        assertEquals(expectedString, bean1.getMyString());
        assertEquals(expectedString, bean2.getMyString());
        assertEquals(expectedDate, bean1.getMyDate());
        assertEquals(expectedDate, bean2.getMyDate());
    }

    @Test
    public void testCopyGetToSetOneString() {
        Bean bean1 = new Bean();
        Bean bean2 = new Bean();

        bean1.setMyString(expectedString);

        ReflexionTools.copyGetToSet(bean1, bean2, "myString");

        assertEquals(expectedString, bean1.getMyString());
        assertEquals(expectedString, bean2.getMyString());
    }

    @Test
    public void testCopyGetToSetOneStringSmallname() {
        Bean bean1 = new Bean();
        Bean bean2 = new Bean();

        bean1.setA(expectedString);

        ReflexionTools.copyGetToSet(bean1, bean2, "myString", "a");

        assertEquals(expectedString, bean1.getA());
        assertEquals(expectedString, bean2.getA());
        assertEquals(null, bean1.getMyString());
        assertEquals(null, bean2.getMyString());
    }

}
