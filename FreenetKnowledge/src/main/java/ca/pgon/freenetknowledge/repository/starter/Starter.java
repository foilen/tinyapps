/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.repository.starter;

/**
 * This interface cleans the table at startup and add some default entries if needed.
 */
public interface Starter {
    /**
     * Called when the application is starting. Should check the DB consistency and create some entries if needed.
     */
    void startup();
}
