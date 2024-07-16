/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

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
