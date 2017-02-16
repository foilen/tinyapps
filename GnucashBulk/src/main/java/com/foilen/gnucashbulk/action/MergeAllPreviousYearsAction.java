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
package com.foilen.gnucashbulk.action;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.foilen.gnucashbulk.GnucashBulkExeception;
import com.foilen.gnucashbulk.domain.Account;
import com.foilen.gnucashbulk.gui.AccountChooser;
import com.foilen.gnucashbulk.gui.Principal;
import com.foilen.gnucashbulk.rowmapper.AccountRowMapper;
import com.foilen.gnucashbulk.service.TransactionService;

public class MergeAllPreviousYearsAction {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final List<String> selectableAccountTypes = Arrays.asList("ASSET", "BANK", "CASH");

    private Principal principal;
    private JdbcTemplate jdbcTemplate;
    private String latestYear;

    private List<Account> selectableAccounts;

    private Account balanceAccount;

    public MergeAllPreviousYearsAction(Principal principal, JdbcTemplate jdbcTemplate, String latestYear) {
        this.principal = principal;
        this.jdbcTemplate = jdbcTemplate;
        this.latestYear = latestYear;
    }

    public void execute() {
        if (latestYear == null) {
            JOptionPane.showMessageDialog(null, "Error", "You do not have any transactions", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Find all the account details
        List<Account> accounts = jdbcTemplate.query("SELECT * FROM accounts", new AccountRowMapper());
        selectableAccounts = new ArrayList<>();
        final Map<String, Account> accountByGuid = new HashMap<>();
        for (Account account : accounts) {
            accountByGuid.put(account.getGuid(), account);
            if (selectableAccountTypes.contains(account.getType())) {
                selectableAccounts.add(account);
            }
        }

        // Calculate the total for each account in the years to remove
        jdbcTemplate.query("SELECT s.account_guid, s.value_num FROM transactions t, splits s WHERE t.post_date not like '" + latestYear + "%' AND t.guid = s.tx_guid", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String accountGuid = rs.getString("account_guid");
                Long amount = rs.getLong("value_num");
                accountByGuid.get(accountGuid).addToTotal(amount);
            }
        });

        // Remove all the transactions and splits of previous years
        jdbcTemplate.update("DELETE FROM slots WHERE obj_guid IN (SELECT s.guid FROM transactions t, splits s WHERE t.post_date not like '" + latestYear + "%' AND t.guid = s.tx_guid)");
        jdbcTemplate.update("DELETE FROM splits WHERE tx_guid IN (SELECT guid FROM transactions WHERE post_date not like '" + latestYear + "%')");

        jdbcTemplate.update("DELETE FROM slots WHERE obj_guid IN (SELECT guid FROM transactions WHERE post_date not like '" + latestYear + "%')");
        jdbcTemplate.update("DELETE FROM transactions WHERE post_date not like '" + latestYear + "%'");

        // Choose which account is the opening balances
        AccountChooser accountChooser = new AccountChooser(this, accounts);
        accountChooser.setVisible(true);
    }

    public Account getBalanceAccount() {
        return balanceAccount;
    }

    public List<Account> getSelectableAccounts() {
        return selectableAccounts;
    }

    public void lastSteps() {

        TransactionService transactionService = new TransactionService(jdbcTemplate);
        long lastYear = Long.valueOf(latestYear) - 1;
        Date openingBalanceDate;
        try {
            openingBalanceDate = sdf.parse(lastYear + "" + "-12-31");
        } catch (ParseException e) {
            throw new GnucashBulkExeception("Problem parsing date", e);
        }

        // Create the opening balances transactions for ASSET, BANK, CASH
        for (Account account : selectableAccounts) {
            if (account == balanceAccount || account.getTotal() == 0) {
                continue;
            }

            transactionService.createSimpleTransaction(openingBalanceDate, balanceAccount, account, "Opening Balance", account.getTotal());

        }

        principal.refreshAll();
    }

    public void setBalanceAccount(Account balanceAccount) {
        this.balanceAccount = balanceAccount;
    }

    public void setSelectableAccounts(List<Account> selectableAccounts) {
        this.selectableAccounts = selectableAccounts;
    }

}
