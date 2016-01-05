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
package ca.pgon.freenetknowledge.repository.creator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class DbCreator extends JdbcTemplate {

    private String[] sqlPrefixes;

    static private final Logger logger = Logger.getLogger(DbCreator.class.getName());

    /**
     * Executes at the start of the application. Executes all the SQL files that were not already executed in the past.
     */
    @PostConstruct
    public void init() {
        logger.info("Starting DbCreator");

        createUpdateTable();

        for (String sqlPrefix : sqlPrefixes) {
            executeAllNewSql(sqlPrefix);
        }

        logger.info("Ending DbCreator");
    }

    /**
     * Creates the update table and ignore any error (if it already exists)
     */
    private void createUpdateTable() {
        String content = getSqlFileContent("/ca/pgon/freenetknowledge/repository/creator/Executed.sql");
        try {
            execute(content);
        } catch (Exception e) {

        }
    }

    /**
     * Execute all the files starting by the prefix and followed by incrementing numbers.
     * 
     * @param sqlPrefix
     *            the prefix
     */
    private void executeAllNewSql(String sqlPrefix) {
        int count = 0;
        boolean done = false;
        while (!done) {
            ++count;

            // Get the file name
            String sqlFile = sqlPrefix + "-" + count + ".sql";

            // Skip if already executed
            if (isAlreadyExecuted(sqlFile)) {
                logger.log(Level.INFO, "Skipping {0}", sqlFile);
                continue;
            }

            // Get the content
            String content = getSqlFileContent(sqlFile);
            if (content == null) {
                done = true;
                continue;
            }

            // Execute
            logger.log(Level.INFO, "Executing {0}", sqlFile);
            logger.log(Level.INFO, "SQL: {0}", content);
            execute(content);

            // Add to the list of already executed
            addToExecuted(sqlFile);
        }

    }

    /**
     * Checks if the file was already executed in the past.
     * 
     * @param sqlFile
     *            the file name
     * @return true if it was executed
     */
    private boolean isAlreadyExecuted(String sqlFile) {
        int count = queryForInt("SELECT count(*) FROM Executed WHERE file=?", sqlFile);

        return count == 1;
    }

    /**
     * Create an entry for that file.
     * 
     * @param sqlFile
     *            the file name
     */
    private void addToExecuted(String sqlFile) {
        update("INSERT INTO Executed (file) values (?)", sqlFile);
    }

    /**
     * Get the content of the resource file and return it.
     * 
     * @param sqlFile
     *            the name of the resource
     * @return the content or null if failing
     */
    private String getSqlFileContent(String sqlFile) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(sqlFile)));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }

            return sb.toString();
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * @return the sqlPrefixes
     */
    public String[] getSqlPrefixes() {
        return sqlPrefixes;
    }

    @Autowired
    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    /**
     * @param sqlPrefixes
     *            the sqlPrefixes to set
     */
    public void setSqlPrefixes(String[] sqlPrefixes) {
        this.sqlPrefixes = sqlPrefixes;
    }

}
