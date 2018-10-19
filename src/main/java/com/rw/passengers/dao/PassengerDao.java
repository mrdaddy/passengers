package com.rw.passengers.dao;

import com.rw.passengers.SQLQueries;
import com.rw.passengers.dto.Passenger;
import com.rw.passengers.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rw.passengers.SQLQueries.*;

@Transactional
@Repository
public class PassengerDao {
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public Passenger createPassenger(Passenger passenger, User user) {


        Map<String, Object> namedParameters = new HashMap();
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameters.put("docTypeCode", passenger.getDocumentType());
        namedParameters.put("passCountryCode", passenger.getCountry());
        namedParameters.put("bd", passenger.getBirthday());
        namedParameters.put("doc_number", passenger.getDocumentNumber());
        namedParameters.put("firstname", passenger.getFirstName());
        namedParameters.put("lastname", passenger.getLastName());
        namedParameters.put("patronymic", passenger.getPatronymic());
        namedParameters.put("user_id", user.getId());

        Integer docTypeId = getId("docTypeCode", passenger.getDocumentType(), GET_DOCTYPE_ID_BY_CODE);
        Integer countryId = getId("passCountryCode", passenger.getCountry(), GET_COUNTRY_ID_BY_CODE);

        namedParameters.put("country", countryId);
        namedParameters.put("doc_type", docTypeId);

        try{
            passenger.getSex().equals(null);
            namedParameters.put("sex", passenger.getSex().toString());
        }catch (NullPointerException e){
            namedParameters.put("sex", passenger.getSex());
        }


        SqlParameterSource parameters = new MapSqlParameterSource(namedParameters);
        namedParameterJdbcTemplate.update(SQLQueries.PASSENGER_CREATE, parameters, keyHolder);
        passenger.setId(keyHolder.getKey().longValue());

        return passenger;
    }

    public Passenger updatePassenger(long passengerId, Passenger passenger, User user) {


        Map<String, Object> namedParameters = new HashMap();

        namedParameters.put("docTypeCode", passenger.getDocumentType());
        namedParameters.put("bd", passenger.getBirthday());
        namedParameters.put("doc_number", passenger.getDocumentNumber());
        namedParameters.put("firstname", passenger.getFirstName());
        namedParameters.put("lastname", passenger.getLastName());
        namedParameters.put("patronymic", passenger.getPatronymic());
        namedParameters.put("user_id", user.getId());
        namedParameters.put("passengerId", passengerId);

        Integer docTypeId = getId("docTypeCode", passenger.getDocumentType(), GET_DOCTYPE_ID_BY_CODE);
        Integer countryId = getId("passCountryCode", passenger.getCountry(), GET_COUNTRY_ID_BY_CODE);

        namedParameters.put("passCountryCode", passenger.getCountry()); namedParameters.put("country", countryId);
        namedParameters.put("doc_type", docTypeId);

        try{
            passenger.getSex().equals(null);
            namedParameters.put("sex", passenger.getSex().toString());
        }catch (NullPointerException e){
            namedParameters.put("sex", passenger.getSex());
        }

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
        passenger.setFirstName(rs.getString("FIRST_NAME"));
        passenger.setLastName(rs.getString("LAST_NAME"));
        passenger.setDocumentNumber(rs.getString("DOCUMENT_NO"));
        passenger.setPatronymic(rs.getString("PATRONYMIC"));

        int countryID = rs.getInt("COUNTRY_ID");
        passenger.setDocumentType(getDocType("id", countryID, GET_COUNTRY_BY_ID, "ISO_CODE"));

        int docTypeId = rs.getInt("DOCUMENT_TYPE_ID");
        passenger.setDocumentType(getDocType("id", docTypeId, GET_DOCTYPE_CODE_BY_ID, "CODE"));

        try {
            passenger.setSex(Passenger.SEX_TYPE.valueOf(rs.getString("SEX")));
        }catch (NullPointerException e){
            passenger.setSex(null);
        }

        return passenger;
    }

    private String getDocType(String map, int param, String request, String columnName) {

        Map<String, Object> namedParameters = new HashMap();
        namedParameters.put(map , param);

        String docType = namedParameterJdbcTemplate
                .queryForObject(request,
                        namedParameters, (rs, rowNum) -> rs.getString(columnName));
        return docType;
    }

    private int getId(String map, String param, String request) {

        Map<String, Object> namedParameters = new HashMap();
        namedParameters.put(map , param);

        Integer docTypeId = namedParameterJdbcTemplate
                .queryForObject(request,
                        namedParameters, (rs, rowNum) -> rs.getInt("ID"));
        return docTypeId;
    }


}
