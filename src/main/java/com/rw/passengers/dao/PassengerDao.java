package com.rw.passengers.dao;

import com.rw.passengers.SQLQueries;
import com.rw.passengers.dto.Passenger;
import com.rw.passengers.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

@Transactional
@Repository
public class PassengerDao {
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public Passenger createPassenger(Passenger passenger, User user) {

        Map namedParameters = new HashMap();
        namedParameters.put("bd", passenger.getBirthday());
        namedParameters.put("country", passenger.getCountry());
        namedParameters.put("doc_number", passenger.getDocumentNumber());
        namedParameters.put("doc_type", passenger.getDocumentType());
        namedParameters.put("firstname", passenger.getFirstName());
        namedParameters.put("lastname", passenger.getLastName());
        namedParameters.put("patronymic", passenger.getPatronymic());
        namedParameters.put("sex", passenger.getSex());
        namedParameters.put("user_id", user.getId());


        namedParameterJdbcTemplate.update(SQLQueries.PASSENGER_CREATE, namedParameters);


        passenger.setId(namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(SQLQueries.PASSENGER_INFO, Passenger.class).getId());

        return passenger;
    }

    public Passenger updatePassenger(long passengerId, Passenger passenger, User user) {


        Map<String, Object> namedParameters = new HashMap();
        namedParameters.put("bd", passenger.getBirthday());
        namedParameters.put("country", passenger.getCountry());
        namedParameters.put("doc_number", passenger.getDocumentNumber());
        namedParameters.put("doc_type", passenger.getDocumentType());
        namedParameters.put("firstname", passenger.getFirstName());
        namedParameters.put("lastname", passenger.getLastName());
        namedParameters.put("patronymic", passenger.getPatronymic());
        namedParameters.put("sex", passenger.getSex());
        namedParameters.put("user_id", user.getId());
        namedParameters.put("passengerId", passengerId);
        namedParameters.put("user_id", user.getId());



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

        Passenger passenger = namedParameterJdbcTemplate.queryForObject(SQLQueries.PASSENGER_INFO, namedParameters, (rs, rowNum) -> getPassengerInfo(rs));
        return passenger;
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
        passenger.setCountry(rs.getString("COUNTRY"));
        passenger.setFirstName(rs.getString("FIRST_NAME"));
        passenger.setLastName(rs.getString("LAST_NAME"));
        if(rs.getString("SEX").equals(Passenger.SEX_TYPE.F)){
            passenger.setSex(Passenger.SEX_TYPE.F);
        }else{passenger.setSex(Passenger.SEX_TYPE.M);}
        passenger.setDocumentNumber(rs.getString("DOCUMENT_NO"));
        passenger.setDocumentType(rs.getString("DOCUMENT_TYPE"));
        passenger.setPatronymic(rs.getString("PATRONYMIC"));
        return passenger;
    }


}
