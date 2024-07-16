/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.CheckMods;

import ca.pgon.saviorlib.FileSystems.FileEntry;

/**
 * This class is used to check if the size says the file needs an update (not the same size)
 */
public class SizeCM implements CheckMod {

    @Override
    public boolean needUpdate(FileEntry source, FileEntry destination) {
        return destination.size != source.size;
    }
    
}
