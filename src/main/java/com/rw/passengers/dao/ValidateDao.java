package com.rw.passengers.dao;

import com.rw.passengers.SQLQueries;
import com.rw.passengers.dto.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.rw.passengers.SQLQueries.GET_DOCTYPE_ID_BY_CODE;

@Repository
public class ValidateDao {

    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public static int validateNameCyr(String name, boolean checkMixed) {
        String alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ -ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        name = name.toUpperCase();
        int minIndex = 61;
        int maxIndex = 0;
        boolean error = false;
        for (int i = 0; i < name.length(); i++) {
            Character valc = name.charAt(i);
            int index = alphabet.indexOf(valc);
            if (index == -1) {
                error = true;
                break;
            } else {
                if (index > maxIndex) {
                    maxIndex = index;
                }
                if (index < minIndex) {
                    minIndex = index;
                }
            }
        }

        if (error)
            return 1;
        else {
            if (checkMixed && minIndex < 33 && maxIndex > 34)
                return 2;
        }

        return 0;
    }


    public static int validateDocNum(String docType, String docNum) {
        String alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ ABCDEFGHIJKLMNOPQRSTUVWXYZ-1234567890/\\";
        int minLength = 2;
        int maxLength = 20;
        String val = docNum.toUpperCase();
        val = val.replace(" ", "");
        if (docType != null) {
            if (docType.equals("ПБ")) {
                alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ 1234567890/\\";
                minLength = 9;
                maxLength = 9;
                if (val.length() >= minLength) {
                    String alphabet1 = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
                    for (int i = 0; i < 2; i++) {
                        Character valc = val.charAt(i);
                        if (alphabet1.indexOf(valc) == -1) {
                            return 1;
                        }
                    }
                    alphabet1 = "1234567890";
                    for (int i = 2; i < 9; i++) {
                        Character valc = val.charAt(i);
                        if (alphabet1.indexOf(valc) == -1) {
                            return 2;
                        }
                    }
                }
            } else if (docType.equals("ПН")) {
                alphabet = "1234567890";
                minLength = 10;
                maxLength = 10;
            } else if (docType.equals("ЗП")) {
                alphabet = "1234567890";
                minLength = 9;
                maxLength = 9;
            } else if (docType.equals("УЛ")) {
                minLength = 1;
            } else if (docType.equals("ПМ")) {
                alphabet = "IXVCDM 1234567890/\\";
                minLength = 9;
                maxLength = 9;
                if (val.length() >= minLength) {
                    String alphabet1 = "IXVCDM";
                    for (int i = 0; i < 2; i++) {
                        Character valc = val.charAt(i);
                        if (alphabet1.indexOf(valc) == -1) {
                            return 3;
                        }
                    }
                    alphabet1 = "1234567890";
                    for (int i = 2; i < 9; i++) {
                        Character valc = val.charAt(i);
                        if (alphabet1.indexOf(valc) == -1) {
                            return 1;
                        }
                    }
                }
            }

        }

        if (val.length() < minLength) {
            return 4;
        }
        if (val.length() > maxLength) {
            return 5;
        }

        for (int i = 0; i < val.length(); i++) {
            Character valc = val.charAt(i);
            if (alphabet.indexOf(valc) == -1) {
                return 6;
            }
        }
        return 0;
    }


    public boolean validateDocNumUnique(String docType, String docNum, long userId) {

        boolean checkUnique = false;
        Map<String, Object> namedParameters = new HashMap();
        namedParameters.put("userId", userId);

        List<Document> documents = namedParameterJdbcTemplate.query(SQLQueries.GET_DOCTYPE_CODES_FOR_USER, namedParameters, (rs, rowNum) -> getDocument(rs));
        Integer docTypeId = getId("docTypeCode", docType, GET_DOCTYPE_ID_BY_CODE);


        try {
            for (Document document : documents) {
                if (document.getId() == docTypeId && document.getNumber().equals(docNum)) {
                    throw new InputMismatchException();
                }
            }
            checkUnique = true;
        } catch (InputMismatchException e) {}

        return checkUnique;
    }


    public static boolean validateBirthday(Date date) {
        if (date != null) {
            Calendar calMin = Calendar.getInstance();
            calMin.set(Calendar.YEAR, 1889);
            Calendar calMax = Calendar.getInstance();
            if (date.getTime() < calMin.getTime().getTime() || date.getTime() > calMax.getTime().getTime()) {
                return false;
            }
        }
        return true;
    }

    private Document getDocument(ResultSet rs) throws SQLException {
        return new Document(rs.getInt("DOCUMENT_TYPE_ID"), rs.getString("DOCUMENT_NO"));
    }

    private int getId(String map, String param, String request) {

        Map<String, Object> namedParameters = new HashMap();
        namedParameters.put(map, param);

        Integer docTypeId = namedParameterJdbcTemplate
                .queryForObject(request,
                        namedParameters, (rs, rowNum) -> rs.getInt("ID"));
        return docTypeId;
    }

}
