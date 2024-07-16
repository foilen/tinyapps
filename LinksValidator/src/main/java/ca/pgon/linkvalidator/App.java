/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.linkvalidator;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        if (args.length == 0) {
            System.out.println("You need to tell at least one URL to check");
            return;
        }

        for (String url : args) {
            SiteCrawler siteCrawler = new SiteCrawler(url);
            siteCrawler.join();
        }
    }

}
