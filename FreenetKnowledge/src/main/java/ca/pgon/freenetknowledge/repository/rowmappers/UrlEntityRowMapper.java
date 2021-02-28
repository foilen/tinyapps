/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.repository.rowmappers;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import ca.pgon.freenetknowledge.repository.entities.UrlEntity;

@Component
public class UrlEntityRowMapper extends BeanPropertyRowMapper<UrlEntity> {

    @Autowired
    private ConversionService conversionService;

    public UrlEntityRowMapper() {
        super(UrlEntity.class);
    }

    @Override
    protected void initBeanWrapper(BeanWrapper bw) {
        bw.setConversionService(conversionService);
    }

}
