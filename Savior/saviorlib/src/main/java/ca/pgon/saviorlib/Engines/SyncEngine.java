/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014

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
