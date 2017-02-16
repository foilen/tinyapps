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
package com.foilen.gnucashbulk;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;

import com.foilen.gnucashbulk.gui.Principal;
import com.foilen.gnucashbulk.rowmapper.StringRowMapper;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Choose the GnuCash file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a GnuCash file (in SQLite format ; not xml)");
        if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "No file selected. Closing application");
            JOptionPane.showMessageDialog(null, "No file selected. Closing application", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Open the file
        String fileLocation = fileChooser.getSelectedFile().getAbsolutePath();
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + fileLocation);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            jdbcTemplate.query("SELECT name FROM SQLITE_MASTER WHERE type = 'table' ORDER BY name", new StringRowMapper());
        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Not an SQLite GnuCash file", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Principal(fileLocation, jdbcTemplate).setVisible(true);

    }

}
