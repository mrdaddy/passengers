package com.rw.passengers.dao;

import com.rw.passengers.SQLQueries;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class ParameterDao implements SQLQueries {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public String getParameterByCode(String code) {
        return getParameterByCode(code, null);
    }

    public String getParameterByCode(String code, String defaultValue) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("CODE", code);
        String param = null;
        try {
            param = jdbcTemplate.queryForObject(
                    PARAM_INFO, params, (rs, rowNum) -> rs.getString("VALUE"));
        } catch (EmptyResultDataAccessException e) {
            log.error("getParameterByCode: can't find parameter: "+code);
        }
        if (param == null) {
            param = defaultValue;
        }
        return param;
    }

}
