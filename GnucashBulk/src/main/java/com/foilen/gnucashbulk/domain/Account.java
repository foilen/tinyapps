/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.gnucashbulk.domain;

public class Account {

    private String guid;
    private String name;
    private String type;

    private long total;

    public Account(String guid, String name, String type) {
        this.guid = guid;
        this.name = name;
        this.type = type;
    }

    public void addToTotal(long amount) {
        total += amount;
    }

    public String getGuid() {
        return guid;
    }

    public String getName() {
        return name;
    }

    public long getTotal() {
        return total;
    }

    public String getType() {
        return type;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Account [guid=");
        builder.append(guid);
        builder.append(", name=");
        builder.append(name);
        builder.append(", type=");
        builder.append(type);
        builder.append(", total=");
        builder.append(total);
        builder.append("]");
        return builder.toString();
    }

}
