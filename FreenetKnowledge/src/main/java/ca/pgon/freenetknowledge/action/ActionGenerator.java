/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.action;

/**
 * This interface defines a class that will create new actions for the ActionController.
 */
public interface ActionGenerator {

    /**
     * This method creates a new action and returns it.
     *
     * @return an action as a Runnable
     */
    Runnable getOne();

}
