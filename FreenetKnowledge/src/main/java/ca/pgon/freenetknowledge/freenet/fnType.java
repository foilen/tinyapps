/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.freenet;

public enum fnType {
    CHK, SSK, USK, KSK;

    public static fnType parse(String type) {
        if (type.compareTo("CHK") == 0) {
            return CHK;
        }
        if (type.compareTo("SSK") == 0) {
            return SSK;
        }
        if (type.compareTo("USK") == 0) {
            return USK;
        }
        if (type.compareTo("KSK") == 0) {
            return KSK;
        }

        return null;
    }
}
