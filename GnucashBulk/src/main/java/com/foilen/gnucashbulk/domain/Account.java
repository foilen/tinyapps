/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2017

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
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
