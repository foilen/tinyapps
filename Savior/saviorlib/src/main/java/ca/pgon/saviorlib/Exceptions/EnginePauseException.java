/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.Exceptions;

@SuppressWarnings("serial")
public class EnginePauseException extends Exception {
    private long bytesRead;
    
    public EnginePauseException(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getBytesRead() {
        return bytesRead;
    }
}
