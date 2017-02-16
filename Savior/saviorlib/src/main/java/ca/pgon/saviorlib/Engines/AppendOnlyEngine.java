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
