/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
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
