package ca.pgon.saviorlib.Events;

import ca.pgon.saviorlib.FileSystems.FileEntry;

public interface DeleteDirectoryEvent {
    void deleteDirectoryEventHandler(FileEntry destination);
}
