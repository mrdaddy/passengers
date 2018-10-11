package com.rw.passengers;

public interface SQLQueries {

        String PASSENGER_INFO = "SELECT ID,USER_ID, LAST_NAME , FIRST_NAME , PATRONYMIC, SEX, BIRTH_DATE," +
                " COUNTRY_ID, DOCUMENT_TYPE_ID, DOCUMENT_NO FROM ETICKET.USER_PASSENGERS WHERE ID = :passengerId AND USER_ID = :user_id";
        String PASSENGERS_INFO = "SELECT ID,USER_ID, LAST_NAME , FIRST_NAME , PATRONYMIC, SEX, BIRTH_DATE, " +
                "                 COUNTRY_ID, DOCUMENT_TYPE_ID, DOCUMENT_NO FROM ETICKET.USER_PASSENGERS WHERE USER_ID = :user_id";


        String PASSENGER_UPDATE = "UPDATE ETICKET.USER_PASSENGERS SET USER_ID = :user_id, LAST_NAME = :lastname, FIRST_NAME = :firstname, PATRONYMIC = :patronymic, SEX = :sex, " +
                "BIRTH_DATE = :bd, COUNTRY_ID = :country, DOCUMENT_TYPE_ID = :doc_type, DOCUMENT_NO = :doc_number WHERE ID = :passengerId AND USER_ID = :user_id";
        String PASSENGER_DELETE = "DELETE FROM ETICKET.USER_PASSENGERS WHERE ID = :passengerId AND USER_ID = :user_id";



        String PASSENGER_CREATE = "INSERT INTO ETICKET.USER_PASSENGERS( USER_ID, LAST_NAME , FIRST_NAME , PATRONYMIC, SEX, BIRTH_DATE, COUNTRY_ID, DOCUMENT_TYPE_ID, DOCUMENT_NO) " +
                " VALUES ( :user_id , :lastname , :firstname , :patronymic , :sex, :bd, :country,:doc_type,:doc_number)";


        String GET_DOCTYPE_ID_BY_CODE = "SELECT ID FROM ETICKET.DOCUMENT_TYPES WHERE CODE = :docTypeCode";

        String GET_COUNTRY_ID_BY_CODE = "SELECT ID FROM ETICKET.PASSENGER_COUNTRIES WHERE ISO_CODE = :passCountryCode";


}
