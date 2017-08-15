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
