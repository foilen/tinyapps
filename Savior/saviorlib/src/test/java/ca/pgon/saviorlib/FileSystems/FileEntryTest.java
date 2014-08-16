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
package ca.pgon.saviorlib.FileSystems;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FileEntryTest {
    private List<FileEntry> expected = new ArrayList<FileEntry>();
    
    @Before
    public void setUp() {
        expected.clear();
        expected.add(FileSystemTools.createFileEntry(false, "first dir/sub_dir", "entry-1", 1000, 1000));
        expected.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-4", 1000, 1000));
        expected.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-5", 1000, 1000));
        expected.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-6", 1000, 1000));
        expected.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-7", 1000, 1000));
        expected.add(FileSystemTools.createFileEntry(false, "second dir/sub_dir", "entry-2", 1000, 1000));
        expected.add(FileSystemTools.createFileEntry(false, "third dir/", "entry-3", 1000, 1000));
    }
    
    @Test
    public void orderingTest() {
        List<FileEntry> actual = new ArrayList<FileEntry>();
        actual.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-6", 1000, 1000));
        actual.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-4", 1000, 1000));
        actual.add(FileSystemTools.createFileEntry(false, "first dir/sub_dir", "entry-1", 1000, 1000));
        actual.add(FileSystemTools.createFileEntry(false, "second dir/sub_dir", "entry-2", 1000, 1000));
        actual.add(FileSystemTools.createFileEntry(false, "third dir/", "entry-3", 1000, 1000));
        actual.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-5", 1000, 1000));
        actual.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-7", 1000, 1000));
        
        Collections.sort(actual);
        
        compareLists(expected, actual);
    }

    private void compareLists(List<FileEntry> expected, List<FileEntry> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i=0; i<expected.size(); ++i) {
            assertEquals("Problem with number: " + i, expected.get(i), actual.get(i));
        }
    }
}
