package com.rw.passengers;

public interface SQLQueries {

        String PASSENGER_INFO = "SELECT CURRENT_TIMESTAMP AS UPDATED_ON, ID,USER_ID, LAST_NAME , FIRST_NAME , PATRONYMIC, SEX, BIRTH_DATE," +
                " COUNTRY, DOCUMENT_TYPE, DOCUMENT_NO FROM ETICKET.USER_PASSENGERS WHERE ID = :passengerID AND USER_ID = :user_id";
        String PASSENGERS_INFO = "SELECT CURRENT_TIMESTAMP AS UPDATED_ON, ID,USER_ID, LAST_NAME , FIRST_NAME , PATRONYMIC, SEX, BIRTH_DATE, " +
                "                 COUNTRY, DOCUMENT_TYPE, DOCUMENT_NO FROM ETICKET.USER_PASSENGERS WHERE USER_ID = :user_id";


        String PASSENGER_UPDATE = "UPDATE ETICKET.USER_PASSENGERS SET (USER_ID = :user_id, LAST_NAME = :lastname, FIRST_NAME = :firstname, PATRONYMIC = :patronymic, SEX = :sex, " +
                "BIRTH_DATE = :bd, COUNTRY = :country, DOCUMENT_TYPE = :doc_type, DOCUMENT_NO = :doc_number) WHERE ID = :passengerId AND USER_ID = :user_id";
        String PASSENGER_DELETE = "DELETE FROM ETICKET.USER_PASSENGERS WHERE ID = :passengerId AND USER_ID = :user_id";



        String PASSENGER_CREATE = "INSERT INTO ETICKET.USER_PASSENGERS( USER_ID, LAST_NAME , FIRST_NAME , PATRONYMIC, SEX, BIRTH_DATE, COUNTRY, DOCUMENT_TYPE, DOCUMENT_NO) " +
                " VALUES ( :user_id , :lastname , :firstname , :patronymic , :sex, :bd, :country,:doc_type,:doc_number)";


}
