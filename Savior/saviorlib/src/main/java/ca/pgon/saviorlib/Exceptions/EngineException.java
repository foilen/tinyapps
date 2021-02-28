/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.Exceptions;

@SuppressWarnings("serial")
public class EngineException extends RuntimeException {
    private Exception exception;
    
    public EngineException(String message) {
        super(message);
    }

    public EngineException(String message, Exception exception) {
        this.exception = exception;
    }
    
    public Exception getExeption() {
        return exception;
    }
}
