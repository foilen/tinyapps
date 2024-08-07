/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.FileSystems;

/**
 * This class contains the informations about a file.
 * The separators in the path is always '/'. The FileSystems will take care of the right separator.
 */
public class FileEntry implements Comparable<FileEntry> {
    public boolean isDirectory;
    public String path;
    public String name;
    public long modificationTime = -1;
    public long size = -1;
    
    public FileSystem fileSystem;
    
    public FileEntry copyClone() {
        FileEntry result = new FileEntry();
        
        result.isDirectory = isDirectory;
        result.path = path;
        result.name = name;
        result.modificationTime = modificationTime;
        result.size = size;
        result.fileSystem = fileSystem;
        
        return result;
    }
    
    @Override
    public int compareTo(FileEntry fileEntry) {
        int result = path.compareTo(fileEntry.path);
        if (result < 0) return -1;
        if (result > 0) return 1;
        
        result = name.compareTo(fileEntry.name);
        if (result < 0) return -1;
        if (result > 0) return 1;
        
        return 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) return false;
        if (!(obj instanceof FileEntry)) return false;
        
        FileEntry fileEntry = (FileEntry) obj;
        if (isDirectory != fileEntry.isDirectory) return false;
        if (!path.equals(fileEntry.path)) return false;
        if (!name.equals(fileEntry.name)) return false;
        if (modificationTime != fileEntry.modificationTime) return false;
        if (size != fileEntry.size) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.isDirectory ? 1 : 0);
        hash = 29 * hash + (this.path != null ? this.path.hashCode() : 0);
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 29 * hash + (int) (this.modificationTime ^ (this.modificationTime >>> 32));
        hash = 29 * hash + (int) (this.size ^ (this.size >>> 32));
        return hash;
    }
    
    @Override
    public String toString() {
        if (isDirectory) {
            return name + '/';
        } else {
            return name;
        }
    }
}
