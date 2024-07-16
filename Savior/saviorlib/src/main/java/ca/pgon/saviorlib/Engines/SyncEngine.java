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
 * This engine removes old files and directory and overwrites and add the files.
 * The destination directory will be exactly like the source.
 */
public class SyncEngine extends AbstractEngine {
    
    @Override
    protected void processDelete(FileEntry destination) {
        if (destination.isDirectory) {
            callDeleteDirectoryEvent(destination);
            destination.fileSystem.deleteDirectory(destination);
        } else {
            callDeleteEvent(destination);
            destination.fileSystem.deleteFile(destination);
        }
    }

    @Override
    protected void processAlreadyExists(FileEntry source, FileEntry destination) {
        if (source.isDirectory || destination.isDirectory) return;
        
        if (checkIfModified(source, destination)) {
            callAddEvent(destination, AddProgressType.STARTING);
            copyFile(source, destination, 0);
            callAddEvent(destination, AddProgressType.COMPLETED);
        }
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
