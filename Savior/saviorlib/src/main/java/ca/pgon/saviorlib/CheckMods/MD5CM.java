/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.CheckMods;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ca.pgon.saviorlib.Exceptions.FileSystemException;
import ca.pgon.saviorlib.FileSystems.FileEntry;

/**
 * This class is used to check if the MD5 hash says the file needs an update.
 * It should only be used locally, because the remote files would be read here and also when copied.
 */
public class MD5CM implements CheckMod {

    @Override
    public boolean needUpdate(FileEntry source, FileEntry destination) {
        return !computeMD5(source.fileSystem.readFile(source)).equals(computeMD5(destination.fileSystem.readFile(destination)));
    }
    
    static public String computeMD5(InputStream in) {
        try {
            MessageDigest digest = null;
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            byte[] b = new byte[1024];
            int len;
            while ( (len = in.read(b)) > 0 ) {
                digest.update(b, 0, len);
            }

            in.close();
            return bytesToHex(digest.digest());
        } catch (NoSuchAlgorithmException ex) {
            throw new FileSystemException("Cannot use MD5");
        } catch (IOException ex) {
            throw new FileSystemException("Error reading file", ex);
        }
        
    }

    static private String bytesToHex(byte[] bytes) {
            String result = "";

            for (byte next : bytes) {
                    String part = Integer.toHexString(0xFF & next);

                    if (part.length() == 1) part = "0" + part;

                    result += part;
            }

            return result;
    }
}
