/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.repository.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ca.pgon.freenetknowledge.repository.dao.SearchUrlDao;
import ca.pgon.freenetknowledge.repository.entities.SearchUrlEntity;
import ca.pgon.freenetknowledge.repository.entities.UrlEntity;
import ca.pgon.freenetknowledge.utils.URLUtils;

@Component
public class JDBCSearchUrlDaoImpl extends JdbcTemplate implements SearchUrlDao {

    @Autowired
    private URLUtils urlUtils;

    private RowMapper<SearchUrlEntity> rowMapper = new BeanPropertyRowMapper<SearchUrlEntity>(SearchUrlEntity.class);

    @Override
    public void createOrUpdateURL(UrlEntity ue) {
        // Update or create
        SearchUrlEntity sue = get(ue.getId());

        String fullUrl = urlUtils.toURLWhitoutBase(ue);
        String fullUrlLowerCase = fullUrl.toLowerCase();

        if (sue == null) {
            update("INSERT INTO SearchUrlEntity (id, fullUrl, fullUrlLowerCase) values (?,?,?)", ue.getId(), fullUrl, fullUrlLowerCase);
        } else {
            update("UPDATE SearchUrlEntity SET fullUrl = ?, fullUrlLowerCase = ? WHERE id = ?", fullUrl, fullUrlLowerCase, ue.getId());
        }

    }

    @Override
    public List<String> findUrlLike(String query) {
        return queryForList("SELECT fullUrl FROM SearchUrlEntity WHERE fullUrlLowerCase LIKE ?", new Object[] { query.toLowerCase() }, String.class);
    }

    @Override
    public SearchUrlEntity get(Long urlId) {
        List<SearchUrlEntity> list = query("SELECT * FROM SearchUrlEntity WHERE id = ?", new Object[] { urlId }, rowMapper);

        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Autowired
    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

}
