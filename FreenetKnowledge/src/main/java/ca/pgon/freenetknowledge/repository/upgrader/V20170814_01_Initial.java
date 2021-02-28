/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.repository.upgrader;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.foilen.smalltools.upgrader.tasks.AbstractDatabaseUpgradeTask;

@Component
public class V20170814_01_Initial extends AbstractDatabaseUpgradeTask {

    @Override
    public void execute() {

        List<String> tableNames = jdbcTemplate.queryForList("SHOW TABLES").stream() //
                .map(it -> it.get("TABLE_SCHEMA") + "." + it.get("TABLE_NAME")) //
                .collect(Collectors.toList());

        // Check if the table already exists
        if (tableNames.size() >= 3) {
            // Upgrade to this upgrader tools
            jdbcTemplate.update("DROP TABLE EXECUTED");
        } else {
            // Initial insert
            updateFromResource("V20170814_01_Initial.sql", getClass());

        }

    }

}
