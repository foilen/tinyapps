package com.foilen.gnucashbulk.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.foilen.gnucashbulk.domain.Account;

public class AccountRowMapper implements RowMapper<Account> {

    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Account(rs.getString("guid"), rs.getString("name"), rs.getString("account_type"));
    }

}
