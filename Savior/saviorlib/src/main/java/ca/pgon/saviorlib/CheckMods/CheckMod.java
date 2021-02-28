/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.CheckMods;

import ca.pgon.saviorlib.FileSystems.FileEntry;

/**
 * This interface defines the function needed to check if a file needs an update
 */
public interface CheckMod {
    /**
     * Tells if the destination file needs an update
     * @param source
     * @param destination
     * @return True if the file should be updated
     */
    boolean needUpdate(FileEntry source, FileEntry destination);
}
