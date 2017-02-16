/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2016

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
package com.foilen.gnucashbulk.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.springframework.jdbc.core.JdbcTemplate;

import com.foilen.gnucashbulk.action.DeleteLatestsYearAction;
import com.foilen.gnucashbulk.action.MergeAllPreviousYearsAction;
import com.foilen.gnucashbulk.rowmapper.StringRowMapper;

public class Principal extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTable tablesDetailsTable;
    private JTable yearsTransactionsTable;

    private JdbcTemplate jdbcTemplate;

    private String latestYear;
    private JButton btnMergeAllPrevious;
    private JButton btnDeleteLatestYear;

    /**
     * Create the frame.
     */
    public Principal(String fileLocation, final JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;

        setTitle("GnuCash Bulk - " + fileLocation);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 952, 629);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        tablesDetailsTable = new JTable();
        tablesDetailsTable.setEnabled(false);
        contentPane.add(new JScrollPane(tablesDetailsTable));

        yearsTransactionsTable = new JTable();
        yearsTransactionsTable.setEnabled(false);
        contentPane.add(new JScrollPane(yearsTransactionsTable));

        btnMergeAllPrevious = new JButton("Merge all previous years transactions into an opening balance");
        final Principal principal = this;
        btnMergeAllPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MergeAllPreviousYearsAction action = new MergeAllPreviousYearsAction(principal, jdbcTemplate, latestYear);
                action.execute();
            }
        });
        contentPane.add(btnMergeAllPrevious);

        btnDeleteLatestYear = new JButton("Delete all the transactions in the latest year");
        btnDeleteLatestYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteLatestsYearAction action = new DeleteLatestsYearAction(principal, jdbcTemplate, latestYear);
                action.execute();
            }
        });
        contentPane.add(btnDeleteLatestYear);

        refreshAll();
    }

    private Object[][] convertToData(List<Object[]> entries) {
        return entries.toArray(new Object[1][]);
    }

    private <K, V> Object[][] convertToData(Map<K, V> countPerYear) {
        List<Object> keys = countPerYear.keySet().stream().sorted().collect(Collectors.toList());
        List<Object[]> entries = new ArrayList<>(countPerYear.size());
        for (Object key : keys) {
            entries.add(new Object[] { key, countPerYear.get(key) });
        }

        return convertToData(entries);
    }

    public void refreshAll() {

        // Get the table names and count
        List<String> tableNames = jdbcTemplate.query("SELECT name FROM SQLITE_MASTER WHERE type = 'table' ORDER BY name", new StringRowMapper());
        List<Object[]> entries = new ArrayList<>(tableNames.size());
        for (String tableName : tableNames) {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Long.class);
            entries.add(new String[] { tableName, String.valueOf(count) });
        }
        tablesDetailsTable.setModel(new DefaultTableModel(convertToData(entries), new String[] { "Table Name", "Entries" }));

        // Get the transactions years
        Map<String, Long> countPerYear = new HashMap<>();
        for (String date : jdbcTemplate.query("SELECT post_date FROM transactions", new StringRowMapper())) {
            String year = date.substring(0, 4);
            if (countPerYear.containsKey(year)) {
                countPerYear.put(year, countPerYear.get(year) + 1);
            } else {
                countPerYear.put(year, 1L);
            }
        }
        yearsTransactionsTable.setModel(new DefaultTableModel(convertToData(countPerYear), new String[] { "Year", "Transaction count" }));

        if (countPerYear.isEmpty()) {
            latestYear = null;
        } else {
            latestYear = countPerYear.keySet().stream().sorted().collect(Collectors.toList()).get(countPerYear.size() - 1);
        }
    }

}
