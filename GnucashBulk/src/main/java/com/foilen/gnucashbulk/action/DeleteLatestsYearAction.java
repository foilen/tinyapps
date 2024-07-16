/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.gnucashbulk.action;

import javax.swing.JOptionPane;

import org.springframework.jdbc.core.JdbcTemplate;

import com.foilen.gnucashbulk.gui.Principal;

public class DeleteLatestsYearAction {

    private Principal principal;
    private JdbcTemplate jdbcTemplate;
    private String latestYear;

    public DeleteLatestsYearAction(Principal principal, JdbcTemplate jdbcTemplate, String latestYear) {
        this.principal = principal;
        this.jdbcTemplate = jdbcTemplate;
        this.latestYear = latestYear;
    }

    public void execute() {
        if (latestYear == null) {
            JOptionPane.showMessageDialog(null, "Error", "You do not have any transactions", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Remove all the transactions and splits of latest year
        jdbcTemplate.update("DELETE FROM slots WHERE obj_guid IN (SELECT s.guid FROM transactions t, splits s WHERE t.post_date like '" + latestYear + "%' AND t.guid = s.tx_guid)");
        jdbcTemplate.update("DELETE FROM splits WHERE tx_guid IN (SELECT guid FROM transactions WHERE post_date like '" + latestYear + "%')");

        jdbcTemplate.update("DELETE FROM slots WHERE obj_guid IN (SELECT guid FROM transactions WHERE post_date like '" + latestYear + "%')");
        jdbcTemplate.update("DELETE FROM transactions WHERE post_date like '" + latestYear + "%'");

        principal.refreshAll();
    }

}
