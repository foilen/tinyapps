/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.chaincommander.configurators.exceptions;

/**
 * Thrown when the ArgumentsConfigurator has an issue parsing the arguments.
 */
public class BadArgumentsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Contructor with a message.
     * 
     * @param msg
     *            the message
     */
    public BadArgumentsException(String msg) {
        super(msg);
    }

}
