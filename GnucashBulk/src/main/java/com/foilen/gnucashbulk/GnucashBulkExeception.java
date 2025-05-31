package com.foilen.gnucashbulk;

public class GnucashBulkExeception extends RuntimeException {

    private static final long serialVersionUID = 2016010801L;

    public GnucashBulkExeception(String message) {
        super(message);
    }

    public GnucashBulkExeception(String message, Throwable cause) {
        super(message, cause);
    }

}
