package com.rw.passengers.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class PassengerDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
}
