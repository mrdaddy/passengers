package com.rw.passengers.dao;

import com.rw.passengers.SQLQueries;
import com.rw.passengers.dto.Passenger;
import com.rw.passengers.security.User;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.security.krb5.internal.PAData;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rw.passengers.SQLQueries.GET_COUNTRY_ID_BY_CODE;
import static com.rw.passengers.SQLQueries.GET_DOCTYPE_ID_BY_CODE;

@Transactional
@Repository
public class PassengerDao {
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public Passenger createPassenger(Passenger passenger, User user) {


        Map<String, Object> namedParameters = new HashMap();

        namedParameters.put("docTypeCode", passenger.getDocumentType());
        namedParameters.put("passCountryCode", passenger.getCountry());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameters.put("country", Integer.valueOf(namedParameterJdbcTemplate.queryForRowSet( GET_COUNTRY_ID_BY_CODE, namedParameters).findColumn("ID")));
        namedParameters.put("doc_type", Integer.valueOf(namedParameterJdbcTemplate.queryForRowSet(GET_DOCTYPE_ID_BY_CODE, namedParameters).findColumn("ID")));

        namedParameters.put("bd", passenger.getBirthday());
        namedParameters.put("doc_number", passenger.getDocumentNumber());
        namedParameters.put("firstname", passenger.getFirstName());
        namedParameters.put("lastname", passenger.getLastName());
        namedParameters.put("patronymic", passenger.getPatronymic());
        namedParameters.put("sex", passenger.getSex().toString());
        namedParameters.put("user_id", user.getId());


        SqlParameterSource parameters = new MapSqlParameterSource(namedParameters);
        namedParameterJdbcTemplate.update(SQLQueries.PASSENGER_CREATE, parameters, keyHolder);
        passenger.setId(keyHolder.getKey().longValue());

        //passenger = namedParameterJdbcTemplate.queryForObject(SQLQueries.PASSENGER_INFO, namedParameters, (rs, rowNum) -> getPassengerInfo(rs));
        //passenger.setId(namedParameterJdbcTemplate.queryForObject(SQLQueries.PASSENGER_INFO, namedParameters, (rs, rowNum) -> getPassengerInfo(rs)).getId());
        return passenger;
    }

    public Passenger updatePassenger(long passengerId, Passenger passenger, User user) {


        Map<String, Object> namedParameters = new HashMap();

        namedParameters.put("docTypeCode", passenger.getDocumentType());
        namedParameters.put("passCountryCode", passenger.getCountry());

        namedParameters.put("country", namedParameterJdbcTemplate.queryForRowSet( GET_COUNTRY_ID_BY_CODE, namedParameters).findColumn("ID"));
        namedParameters.put("doc_type", namedParameterJdbcTemplate.queryForRowSet(GET_DOCTYPE_ID_BY_CODE, namedParameters).findColumn("ID"));

        namedParameters.put("bd", passenger.getBirthday());
        namedParameters.put("doc_number", passenger.getDocumentNumber());
        namedParameters.put("firstname", passenger.getFirstName());
        namedParameters.put("lastname", passenger.getLastName());
        namedParameters.put("patronymic", passenger.getPatronymic());
        namedParameters.put("sex", passenger.getSex().toString());
        namedParameters.put("user_id", user.getId());
        namedParameters.put("passengerId", passengerId);

        namedParameterJdbcTemplate.update(SQLQueries.PASSENGER_UPDATE, namedParameters);
        return passenger;

    }

    public void deletePassenger(long passengerId, User user) {

        Map<String, Object> namedParameters = new HashMap();
        namedParameters.put("user_id", user.getId());
        namedParameters.put("passengerId", passengerId);

        namedParameterJdbcTemplate.update(SQLQueries.PASSENGER_DELETE, namedParameters);
    }

    public Passenger getPassenger(long passengerId, User user) {

        Map<String, Object> namedParameters = new HashMap();
        namedParameters.put("passengerId", passengerId);
        namedParameters.put("user_id", user.getId());

        return namedParameterJdbcTemplate.queryForObject(SQLQueries.PASSENGER_INFO, namedParameters, (rs, rowNum) -> getPassengerInfo(rs));

    }


    public List<Passenger> getPassengers(User user) {

        Map<String, Object> namedParameters = new HashMap();
        namedParameters.put("user_id", user.getId());

        List<Passenger> passengers = namedParameterJdbcTemplate.query(SQLQueries.PASSENGERS_INFO, namedParameters, (rs, rowNum) -> getPassengerInfo(rs));
        return passengers;
    }

    private Passenger getPassengerInfo(ResultSet rs) throws SQLException {

        Passenger passenger = new Passenger();
        passenger.setId(rs.getInt("ID"));
        passenger.setBirthday(rs.getDate("BIRTH_DATE"));
        passenger.setCountry(rs.getString("COUNTRY_ID"));
        passenger.setFirstName(rs.getString("FIRST_NAME"));
        passenger.setLastName(rs.getString("LAST_NAME"));
        if(rs.getString("SEX").equals(Passenger.SEX_TYPE.F)){
            passenger.setSex(Passenger.SEX_TYPE.F);
        }else{passenger.setSex(Passenger.SEX_TYPE.M);}
        passenger.setDocumentNumber(rs.getString("DOCUMENT_NO"));
        passenger.setDocumentType(rs.getString("DOCUMENT_TYPE_ID"));
        passenger.setPatronymic(rs.getString("PATRONYMIC"));
        return passenger;
    }


}
