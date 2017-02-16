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
