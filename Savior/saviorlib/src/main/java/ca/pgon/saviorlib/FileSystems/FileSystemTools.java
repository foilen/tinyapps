/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.FileSystems;

/**
 * This class has some functions to help with the filesystem classes
 */
public class FileSystemTools {
    /**
     * Creates a FileEntry and sets the fields
     * 
     * @param isDirectory
     * @param path
     * @param name
     * @param modificationTime
     * @param size
     * @return The FileEntry
     */
    static public FileEntry createFileEntry(boolean isDirectory, String path, String name, long modificationTime, long size) {
        FileEntry fileEntry = new FileEntry();
        fileEntry.isDirectory = isDirectory;
        fileEntry.path = path;
        fileEntry.name = name;
        fileEntry.modificationTime = modificationTime;
        fileEntry.size = size;
        return fileEntry;
    }

    /**
     * Creates a FileEntry and sets the fields
     * 
     * @param fileSystem 
     * @param isDirectory
     * @param path
     * @param name
     * @param modificationTime
     * @param size
     * @return The FileEntry
     */
    static public FileEntry createFileEntry(FileSystem fileSystem, boolean isDirectory, String path, String name, long modificationTime, long size) {
        FileEntry fileEntry = createFileEntry(isDirectory, path, name, modificationTime, size);
        fileEntry.fileSystem = fileSystem;
        return fileEntry;
    }
    
    static public String getAbsolutePath(FileEntry fileEntry) {
        return getAbsolutePath(fileEntry, fileEntry.fileSystem.getBasePath());
    }
    
    static public String getAbsolutePath(FileEntry fileEntry, String basePath) {
        String absolutePath;
        
        if (basePath.isEmpty()) {
            absolutePath = "";
        } else {
            absolutePath = basePath + '/';
        }
        if (fileEntry != null) {
            if (!fileEntry.path.isEmpty()) {
                absolutePath += fileEntry.path + '/';
            }
            
            absolutePath += fileEntry.name;
        }
        
        absolutePath = absolutePath.replace("//", "/");
        
        return absolutePath;
    }
    
    static public String getRelativePath(FileEntry fileEntry) {
        String relativePath = "";
        if (fileEntry != null) {
            if (fileEntry.path.isEmpty()) {
                relativePath = fileEntry.name;
            } else {
                relativePath = fileEntry.path + '/' + fileEntry.name;
            }
        }
        
        relativePath = relativePath.replace("//", "/");
        
        return relativePath;
    }
}
