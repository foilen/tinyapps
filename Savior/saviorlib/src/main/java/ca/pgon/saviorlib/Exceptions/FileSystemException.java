/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.Exceptions;

@SuppressWarnings("serial")
public class FileSystemException extends RuntimeException {
    private Exception exception;
    
    public FileSystemException(String message) {
        super(message);
    }

    public FileSystemException(String message, Exception exception) {
        super(message);
        this.exception = exception;
    }

    public Exception getExeption() {
        return exception;
    }
}
