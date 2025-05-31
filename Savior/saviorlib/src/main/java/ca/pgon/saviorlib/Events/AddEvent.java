package ca.pgon.saviorlib.Events;

import ca.pgon.saviorlib.FileSystems.FileEntry;

public interface AddEvent {
    void addEventHandler(FileEntry destination, AddProgressType progessType);
}
