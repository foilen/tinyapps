/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.Engines;

import ca.pgon.saviorlib.Events.AddProgressType;
import ca.pgon.saviorlib.FileSystems.FileEntry;

/**
 * This engine only copies the files in the source that are not currently in the destination.
 * The files already in the destination directory stay the same and new files are added.
 */
public class AppendOnlyEngine extends AbstractEngine {

    @Override
    protected void processDelete(FileEntry destination) {
    }

    @Override
    protected void processAlreadyExists(FileEntry source, FileEntry destination) {
    }

    @Override
    protected void processAdd(FileEntry source) {
        FileEntry destination = source.copyClone();
        destination.fileSystem = destinationFS;
        
        if (destination.isDirectory) {
            callCreateDirectoryEvent(destination);
            destination.fileSystem.createDirectory(destination);
        } else {
            callAddEvent(destination, AddProgressType.STARTING);
            copyFile(source, destination, 0);
            callAddEvent(destination, AddProgressType.COMPLETED);
        }
    }

}
