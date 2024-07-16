/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorgui.profile;

import java.util.List;
import java.util.Map;

public class Profile {
    public String sourceFileSystemType;
    public String sourceBasePath;
    public Map<String, String> sourceParams;
    
    public String destinationFileSystemType;
    public String destinationBasePath;
    public Map<String, String> destinationParams;
    
    public String engineType;
    public boolean modDate, modSize, modMD5;
    public List<String> ignoreList;
}
