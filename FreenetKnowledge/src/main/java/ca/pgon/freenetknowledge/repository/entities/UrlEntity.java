/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.repository.entities;

import java.util.Date;

import ca.pgon.freenetknowledge.freenet.fnType;
import ca.pgon.freenetknowledge.utils.ReflexionTools;

/**
 * The description of a Freenet url.
 */
public class UrlEntity {
    // For DB
    private long id;

    // The url
    private fnType type;
    private String hash = "";
    private String name = "";
    private String version = "";
    private String path = "";

    // File info
    private String size;

    // The crawling
    private Date last_visited;
    private boolean error = false;
    private boolean visiting = false;
    private boolean visited = false;

    /**
     * To copy all the fields.
     *
     * @param ue
     *            the source to copy from
     */
    public void copyData(UrlEntity ue) {
        ReflexionTools.copyGetToSet(ue, this, "type", "hash", "name", "version", "path", "size", "last_visited");

        ReflexionTools.copyIsToSet(ue, this, "error", "visiting", "visited");
    }

    public String getHash() {
        return hash;
    }

    public long getId() {
        return id;
    }

    public Date getLast_visited() {
        return last_visited;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getSize() {
        return size;
    }

    public fnType getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public boolean isError() {
        return error;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isVisiting() {
        return visiting;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLast_visited(Date last_visited) {
        this.last_visited = last_visited;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setType(fnType type) {
        this.type = type;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setVisiting(boolean visiting) {
        this.visiting = visiting;
    }

}
