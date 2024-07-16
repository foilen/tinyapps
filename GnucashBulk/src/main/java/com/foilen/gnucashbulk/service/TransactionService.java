/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.gnucashbulk.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.foilen.gnucashbulk.GnucashBulkExeception;
import com.foilen.gnucashbulk.domain.Account;
import com.foilen.gnucashbulk.rowmapper.StringRowMapper;
import com.foilen.smalltools.tools.SecureRandomTools;

public class TransactionService {

    private static final SimpleDateFormat fullSdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert transactionJdbcInsert;
    private SimpleJdbcInsert slotJdbcInsert;
    private SimpleJdbcInsert splitJdbcInsert;

    public TransactionService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        transactionJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        transactionJdbcInsert.setTableName("transactions");
        splitJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        splitJdbcInsert.setTableName("splits");
        slotJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        slotJdbcInsert.setTableName("slots");
    }

    public void createSimpleTransaction(Date date, Account fromAccount, Account toAccount, String description, long amount) {

        // Get the currency
        List<String> currencies = jdbcTemplate.query("SELECT guid FROM commodities WHERE namespace = 'CURRENCY'", new StringRowMapper());
        if (currencies.size() != 1) {
            throw new GnucashBulkExeception("There must be only one currency. Currently, there are " + currencies.size());
        }
        String currencyGuid = currencies.get(0);

        // Create transaction
        String txGuid = genGuid();
        String postDate = fullSdf.format(date);
        String enterDate = fullSdf.format(new Date());

        createTransaction(txGuid, currencyGuid, postDate, enterDate, "", description);

        // Create splits
        createSplit(txGuid, fromAccount.getGuid(), -amount);
        createSplit(txGuid, toAccount.getGuid(), amount);

        // Create slots
        createSlot(txGuid, "date-posted", 10, dateSdf.format(date));
    }

    private void createSlot(String objGuid, String name, int slotType, String gDate) {
        Map<String, Object> args = new HashMap<>();
        args.put("obj_guid", objGuid);
        args.put("name", name);
        args.put("slot_type", slotType);
        args.put("numeric_val_denom", 1);
        args.put("gdate_val", gDate);
        slotJdbcInsert.execute(args);
    }

    private String createSplit(String txGuid, String accountGuid, long amount) {
        Map<String, Object> args = new HashMap<>();
        String guid = genGuid();
        args.put("guid", guid);
        args.put("tx_guid", txGuid);
        args.put("account_guid", accountGuid);
        args.put("memo", "");
        args.put("action", "");
        args.put("reconcile_state", "n");
        args.put("value_num", amount);
        args.put("value_denom", 100);
        args.put("quantity_num", amount);
        args.put("quantity_denom", 100);
        splitJdbcInsert.execute(args);

        return guid;
    }

    private void createTransaction(String txGuid, String currencyGuid, String postDate, String enterDate, String num, String description) {
        Map<String, Object> args = new HashMap<>();
        args.put("guid", txGuid);
        args.put("currency_guid", currencyGuid);
        args.put("num", num);
        args.put("post_date", postDate);
        args.put("enter_date", enterDate);
        args.put("description", description);
        transactionJdbcInsert.execute(args);
    }

    private String genGuid() {
        return SecureRandomTools.randomHexString(32).toLowerCase();
    }

}
