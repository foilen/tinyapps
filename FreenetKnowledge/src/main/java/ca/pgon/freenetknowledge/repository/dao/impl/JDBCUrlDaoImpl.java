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
package ca.pgon.freenetknowledge.repository.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ca.pgon.freenetknowledge.freenet.fnType;
import ca.pgon.freenetknowledge.repository.dao.UrlDao;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

@Component
public class JDBCUrlDaoImpl extends JdbcTemplate implements UrlDao {

    @Autowired
    private RowMapper<UrlEntity> urlEntityRowMapper;

    @Override
    public void createURL(final UrlEntity ue) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO UrlEntity (type, hash, name, version, path, size, last_visited, error, visiting) values (?,?,?,?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);

                ps.setInt(1, ue.getType().ordinal());
                ps.setString(2, ue.getHash());
                ps.setString(3, ue.getName());
                ps.setString(4, ue.getVersion());
                ps.setString(5, ue.getPath());
                ps.setString(6, ue.getSize());
                if (ue.getLast_visited() == null) {
                    ps.setDate(7, null);
                } else {
                    ps.setDate(7, new Date(ue.getLast_visited().getTime()));
                }
                // TODO Future - Do a function for Boolean
                ps.setInt(8, ue.isError() ? 1 : 0);
                ps.setInt(9, ue.isVisiting() ? 1 : 0);

                return ps;
            }
        }, keyHolder);

        Long generatedId = new Long(keyHolder.getKey().longValue());
        ue.setId(generatedId);
    }

    @Override
    public void deleteUrl(long urlId) {
        update("DELETE FROM UrlEntity WHERE id = ?", urlId);
    }

    @Override
    public UrlEntity findSimilar(fnType type, String hash, String name, String path) {
        UrlEntity result = null;

        result = queryForOneObject("SELECT * FROM UrlEntity WHERE type = ? AND hash = ? AND name = ? AND path = ?", new Object[] { type.ordinal(), hash, name, path });

        return result;
    }

    @Override
    public UrlEntity get(long urlId) {
        List<UrlEntity> list = query("SELECT * FROM UrlEntity WHERE id = ?", new Object[] { urlId }, urlEntityRowMapper);

        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public long getCount() {
        return queryForObject("SELECT count(*) FROM UrlEntity", Long.class);
    }

    @Override
    public long getErrorCount() {
        return queryForObject("SELECT count(*) FROM UrlEntity WHERE error = 1", Long.class);
    }

    @Override
    public UrlEntity getNextToVisit() {
        // TODO Future - Limit the amount got
        // TODO Future - One method to retrieve only one
        List<UrlEntity> list = query("SELECT * FROM UrlEntity WHERE visiting = 0 AND error = 0 AND visited = 0", urlEntityRowMapper);
        if (list.isEmpty()) {
            return null;
        } else {
            UrlEntity result = list.get(0);

            update("UPDATE UrlEntity SET visiting = 1 WHERE id = ?", result.getId());

            return result;
        }
    }

    @Override
    public long getToVisitCount() {
        return queryForObject("SELECT count(*) FROM UrlEntity WHERE visiting = 0 AND visited = 0 AND error = 0", Long.class);
    }

    @Override
    public long getVisitedCount() {
        return queryForObject("SELECT count(*) FROM UrlEntity WHERE visited = 1", Long.class);
    }

    /**
     * Get one object from the DB or return null if none.
     * 
     * @param sql
     *            the query
     * @param parameters
     *            the parameters
     * @param hasPath
     *            true if there is a path
     * @return the object or null
     */
    private UrlEntity queryForOneObject(String sql, Object[] parameters) {
        UrlEntity result = null;

        try {
            List<UrlEntity> all = query(sql, parameters, urlEntityRowMapper);
            // Check empty
            if (!all.isEmpty()) {
                // Get the first one
                result = all.get(0);
            }
        } catch (Exception e) {
        }

        return result;
    }

    @Override
    public void resetErrors() {
        update("UPDATE UrlEntity SET error = 0 WHERE error = 1");
    }

    @Override
    public void resetVisited() {
        update("UPDATE UrlEntity SET visited = 0 WHERE error = 0");
    }

    @Override
    public void resetVisiting() {
        update("UPDATE UrlEntity SET visiting = 0");
    }

    @Autowired
    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public void updateURLignoringVersionning(UrlEntity ue) {
        Date date = null;
        if (ue.getLast_visited() != null) {
            date = new Date(ue.getLast_visited().getTime());
        }

        update("UPDATE UrlEntity SET type = ?, hash = ?, name = ?, version = ?, path = ?, size = ?, last_visited = ?, error = ?, visiting = ?, visited = ? WHERE id = ?", ue.getType().ordinal(),
                ue.getHash(), ue.getName(), ue.getVersion(), ue.getPath(), ue.getSize(), date, ue.isError() ? 1 : 0, ue.isVisiting() ? 1 : 0, ue.isVisited() ? 1 : 0, ue.getId());
    }
}
