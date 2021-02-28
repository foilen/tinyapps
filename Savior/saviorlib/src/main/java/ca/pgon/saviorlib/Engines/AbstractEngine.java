/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.Engines;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.pgon.saviorlib.CheckMods.CheckMod;
import ca.pgon.saviorlib.Events.AddEvent;
import ca.pgon.saviorlib.Events.AddProgressType;
import ca.pgon.saviorlib.Events.BackupEvent;
import ca.pgon.saviorlib.Events.BackupType;
import ca.pgon.saviorlib.Events.ChangeDirectoryEvent;
import ca.pgon.saviorlib.Events.CreateDirectoryEvent;
import ca.pgon.saviorlib.Events.DeleteDirectoryEvent;
import ca.pgon.saviorlib.Events.DeleteEvent;
import ca.pgon.saviorlib.Events.ProgressEvent;
import ca.pgon.saviorlib.Exceptions.EngineException;
import ca.pgon.saviorlib.Exceptions.EnginePauseException;
import ca.pgon.saviorlib.Exceptions.FileSystemException;
import ca.pgon.saviorlib.FileSystems.FileEntry;
import ca.pgon.saviorlib.FileSystems.FileSystem;
import ca.pgon.saviorlib.FileSystems.FileSystemTools;

/**
 * This class contains all the common operations to execute the backup.
 * You can easily create an engine by extending this class and implementing 3 simple methods
 */
abstract public class AbstractEngine implements Engine {
    static private final Logger logger = Logger.getLogger(AbstractEngine.class.getName());
    
    protected FileSystem sourceFS, destinationFS;
    protected List<CheckMod> checkMods;
    protected List<String> ignoreFiles;
    
    protected AddEvent addEvent;
    protected BackupEvent backupEvent;
    protected ChangeDirectoryEvent changeDirectoryEvent;
    protected CreateDirectoryEvent createDirectoryEvent;
    protected DeleteEvent deleteEvent;
    protected ProgressEvent progressEvent;
    protected DeleteDirectoryEvent deleteDirectoryEvent;
    
    protected AtomicBoolean requestStop = new AtomicBoolean(false);
    protected AtomicBoolean requestPause = new AtomicBoolean(false);
    protected AtomicBoolean requestResume = new AtomicBoolean(false);
    
    @Override
    public void setSourceFileSystem(FileSystem sourceFS) {
        this.sourceFS = sourceFS;
    }

    @Override
    public void setDestinationFileSystem(FileSystem destinationFS) {
        this.destinationFS = destinationFS;
    }

    @Override
    public void setCheckMods(List<CheckMod> checkMods) {
        this.checkMods = checkMods;
    }
    
    @Override
    public void setIgnoreFiles(List<String> ignoreFiles) {
        this.ignoreFiles = ignoreFiles;
    }
    
    @Override
    public void setAddEvent(AddEvent addEvent) {
        this.addEvent = addEvent;
    }
    
    @Override
    public void setBackupEvent(BackupEvent backupEvent) {
        this.backupEvent = backupEvent;
    }
    
    @Override
    public void setChangeDirectoryEvent(ChangeDirectoryEvent changeDirectoryEvent) {
        this.changeDirectoryEvent = changeDirectoryEvent;
    }
    
    @Override
    public void setCreateDirectoryEvent(CreateDirectoryEvent createDirectoryEvent) {
        this.createDirectoryEvent = createDirectoryEvent;
    }
    
    @Override
    public void setDeleteDirectoryEvent(DeleteDirectoryEvent deleteDirectoryEvent) {
        this.deleteDirectoryEvent = deleteDirectoryEvent;
    }
    
    @Override
    public void setDeleteEvent(DeleteEvent deleteEvent) {
        this.deleteEvent = deleteEvent;
    }
    
    @Override
    public void setProgressEvent(ProgressEvent progressEvent) {
        this.progressEvent = progressEvent;
    }
    
    @Override
    public void start() {
        checkValidEngine();
        
        callBackupEvent(BackupType.STARTING);
        
        try {
            processDirectory(null);
            callBackupEvent(BackupType.COMPLETED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while doing the backup", e);
            callBackupEvent(BackupType.ABORTING);
        }
    }
    
    @Override
    public void pause() {
        callBackupEvent(BackupType.PAUSING);
        requestPause.set(true);
        requestResume.set(false);
    }
    
    @Override
    public void resume() {
        callBackupEvent(BackupType.RESUMING);
        requestResume.set(true);
        requestPause.set(false);
    }

    @Override
    public void stop() {
        requestStop.set(true);
    }
    
    /**
     * Will throw exceptions if the filesystems are not set.
     * Will also check if the filesystems are accessible
     * You can call this as the first function of start()
     */
    protected void checkValidEngine() {
        if (sourceFS == null) {
            throw new EngineException("The source file system is not defined");
        }
        
        if (destinationFS == null) {
            throw new EngineException("The destination file system is not defined");
        }
        
        try {
            sourceFS.checkIfValid();
        } catch (FileSystemException ex) {
            throw new EngineException("The source file system is not accessible", ex);
        }
        
        try {
            destinationFS.checkIfValid();
        } catch (FileSystemException ex) {
            throw new EngineException("The destination file system is not accessible", ex);
        }
        
        if (ignoreFiles == null) {
            ignoreFiles = new ArrayList<String>();
        }
    }
    
    /**
     * Check if the file needs to be updated by using the full checkMods list
     * @param source
     * @param destination
     * @return True if the file should be updated
     */
    protected boolean checkIfModified(FileEntry source, FileEntry destination) {
        if (checkMods == null || checkMods.isEmpty()) {
            throw new EngineException("There are no checkMods defined");
        }
        
        for (CheckMod next: checkMods) {
            if (next.needUpdate(source, destination)) return true;
        }
        
        return false;
    }
    
    /**
     * This function will process all the directories and the files in the sourceEntry.
     * It will call the different process functions
     * @param sourceEntry 
     */
    protected void processDirectory(FileEntry sourceEntry) {
        callChangeDirectoryEvent(sourceEntry);
        
        List<FileEntry> sourceList = sourceFS.listDirectory(sourceEntry);
        List<FileEntry> destinationList = destinationFS.listDirectory(sourceEntry);
        
        removeIgnoredFiles(sourceList);
        removeIgnoredFiles(destinationList);
        
        Collections.sort(sourceList);
        Collections.sort(destinationList);
        
        // Add, delete, modify
        int posSource = 0, posDestination = 0;
        while ((posSource < sourceList.size()) && (posDestination < destinationList.size())) {
            stopIfNeeded();
            pauseIfNeeded();
            
            FileEntry source = sourceList.get(posSource);
            FileEntry destination = destinationList.get(posDestination);
            
            switch (source.compareTo(destination)) {
                case -1:
                    processAdd(source);
                    ++posSource;
                    break;
                case 0:
                    processAlreadyExists(source, destination);
                    ++posSource;
                    ++posDestination;
                    break;
                case 1:
                    processDelete(destination);
                    ++posDestination;
                    break;
            }
        }
        
        while (posSource < sourceList.size()) {
            stopIfNeeded();
            pauseIfNeeded();
            
            processAdd(sourceList.get(posSource++));
        }
        
        while (posDestination < destinationList.size()) {
            stopIfNeeded();
            pauseIfNeeded();
            
            processDelete(destinationList.get(posDestination++));
        }
        
        // Process sub-dirs
        for(FileEntry next: sourceList) {
            stopIfNeeded();
            pauseIfNeeded();
            
            if (next.isDirectory) processDirectory(next);
        }
    }
    
    private void removeIgnoredFiles(List<FileEntry> entries) {
        Iterator<FileEntry> it = entries.iterator();
        while (it.hasNext()) {
            FileEntry fe = it.next();
            String relative = FileSystemTools.getRelativePath(fe);
            
            for (String i: ignoreFiles) {
                if (i.equals(relative)) {
                    it.remove();
                    break;
                }
            }
        }
    }
    
    /**
     * Copy the files from source to destination and handle the pausing/resume
     * @param source
     * @param destination
     * @param offset 
     */
    protected void copyFile(FileEntry source, FileEntry destination, long offset) {
        boolean completed = false;
        
        while (!completed) {
            try {
                OutputStream out;
                InputStream in;
                
                if (offset == 0) {
                    out = destination.fileSystem.createFile(destination);
                    in = source.fileSystem.readFile(source);
                } else {
                    out = destination.fileSystem.appendFile(destination);
                    in = source.fileSystem.readFileFrom(source, offset);
                }
            
                copyStream(in, out, destination, offset);
                
                try {
                    destination.fileSystem.changeFileModificationTime(destination, source.modificationTime);
                } catch (FileSystemException e) { /* Normal for FTP */ }
            
                completed = true;
            } catch (EnginePauseException ex) {
                offset = ex.getBytesRead();
                while(!requestResume.get()) {
                    stopIfNeeded();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
    
    /**
     * Copy the stream from in to out
     * @param in
     * @param out
     * @param destination
     * @throws EnginePauseException 
     */
    protected void copyStream(InputStream in, OutputStream out, FileEntry destination, long offset) throws EnginePauseException {
        try {
            BufferedInputStream bin = new BufferedInputStream(in);
            long count = offset;
            
            byte[] b = new byte[1024];
            int len;
            
            while ( (len = readOnlyAvailable(bin, b, 1024)) != -1) {
                if (requestStop.get()) {
                    bin.close();
                    out.close();
                    throw new EngineException("User requested stop");
                }
                if (requestPause.get()) {
                    requestPause.set(false);
                    try {bin.close();} catch (FileSystemException e) {}
                    try {out.close();} catch (FileSystemException e) {}
                    throw new EnginePauseException(count);
                }
                callProgressEvent(destination, count);
                out.write(b, 0, len);
                count += len;
            }
            
            bin.close();
            out.close();
        } catch (IOException ex) {
            throw new EngineException("Error copying the stream", ex);
        }
    }
    
    private int readOnlyAvailable(BufferedInputStream in, byte[] b, int max) throws IOException {
        int len = in.available();
        if (len == 0) len = 1; // To be able to get the end of file
        if (len < 0) { // Fix a bug in BufferedInputStream that will return a negative value if bigger than 2G
            len = Integer.MAX_VALUE;
        }
        return in.read(b, 0, len>max?max:len);
    }
    
    /**
     * Call the event if set
     * @param destination
     * @param progessType 
     */
    protected void callAddEvent(FileEntry destination, AddProgressType progessType) {
        if (addEvent != null) addEvent.addEventHandler(destination, progessType);
    }
    
    /**
     * Call the event if set
     * @param backupType 
     */
    protected void callBackupEvent(BackupType backupType) {
        if (backupEvent != null) backupEvent.backupEventHandler(backupType);
    }
    
    /**
     * Call the event if set
     * @param source 
     */
    protected void callChangeDirectoryEvent(FileEntry source) {
        if (changeDirectoryEvent != null) changeDirectoryEvent.changeDirectoryEventHandler(source);
    }
    
    /**
     * Call the event if set
     * @param destination 
     */
    protected void callCreateDirectoryEvent(FileEntry destination) {
        if (createDirectoryEvent != null) createDirectoryEvent.createDirectoryEventHandler(destination);
    }
    
    /**
     * Call the event if set
     * @param destination 
     */
    protected void callDeleteDirectoryEvent(FileEntry destination) {
        if (deleteDirectoryEvent != null) deleteDirectoryEvent.deleteDirectoryEventHandler(destination);
    }
    
    /**
     * Call the event if set
     * @param destination 
     */
    protected void callDeleteEvent(FileEntry destination) {
        if (deleteEvent != null) deleteEvent.deleteEventHandler(destination);
    }
    
    /**
     * Call the event if set
     * @param destination
     * @param currentPos
     */
    protected void callProgressEvent(FileEntry destination, long currentPos) {
        if (progressEvent != null) progressEvent.progressEventHandler(destination, currentPos);
    }
    
    /**
     * Called by processDirectory when the file or directory is present on the destination, but not in the source
     * @param destination 
     */
    protected abstract void processDelete(FileEntry destination);
    
    /**
     * Called by processDirectory when the file or directory is present on both the source and the destination
     * @param source
     * @param destination 
     */
    protected abstract void processAlreadyExists(FileEntry source, FileEntry destination);
    
    /**
     * Called by processDirectory when the file or directory is present on the source, but not in the destination
     * @param source 
     */
    protected abstract void processAdd(FileEntry source);

    private void pauseIfNeeded() {
        if (requestPause.get()) {
            while(!requestResume.get()) {
                stopIfNeeded();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
    
    private void stopIfNeeded() {
        if (requestStop.get()) {
            throw new EngineException("User requested stop");
        }
    }
}
