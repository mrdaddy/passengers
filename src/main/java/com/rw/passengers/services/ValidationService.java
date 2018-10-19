package com.rw.passengers.services;

import com.rw.passengers.dao.ValidateDao;
import com.rw.passengers.dto.Passenger;
import com.rw.passengers.validator.PassengerInformationValidator;
import com.sun.jdi.request.ExceptionRequest;
import com.sun.tools.classfile.Code_attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.util.Date;

@Service
public class ValidationService {

    @Autowired
    private ValidateDao validateDao;


    public void validatePassengerInformation(Passenger passenger, Errors e) {

        int errorFormatCode = 0;

        errorFormatCode = validateDao.validateNameCyr(passenger.getFirstName(), false);
        addNameError(e, errorFormatCode, "firstName");


        errorFormatCode = validateDao.validateNameCyr(passenger.getLastName(), true);
        addNameError(e, errorFormatCode, "lastName");


        if (!StringUtils.isEmpty(passenger.getPatronymic())) {
            errorFormatCode = validateDao.validateNameCyr(passenger.getPatronymic(), false);
            addNameError(e, errorFormatCode, "patronymic");
        }

        errorFormatCode = validateDao.validateDocNum(passenger.getDocumentType(), passenger.getDocumentNumber());
        addNumDocError(e, errorFormatCode);
    }

    public void validateDocTypeUnique(String docType, String docNumber, long userId) throws ExceptionInInitializerError {

        boolean check = validateDao.validateDocNumUnique(docType, docNumber, userId);
        if (!check) {
            throw new ExceptionInInitializerError();
        }
    }


    public void validateBirthday(Date birthday, Errors e) {

            boolean checkBirthday = validateDao.validateBirthday(birthday);
            if (!checkBirthday) {
                e.rejectValue("birthday" + PassengerInformationValidator.ERROR_CODES.IncorrectValue.name() + "_BirthdayFormat",
                        "Дата рождения введена некорректно");
            }
    }

    private void addNameError(Errors e, int errorFormatCode, String field) {
        if (errorFormatCode > 0) {
            String code;
            String desc = "";
            switch (errorFormatCode) {
                case 1:
                    desc = "Введены некорректные символы для данного поля";
                    break;
                case 2:
                    desc = "Фамилия пассажира должна быть введена либо буквами кириллицы, либо буквами латиницы. Сочетание букв кириллицы и латиницы в фамилии не допускается";
                    break;
                case 3:
                    desc = "Введены некорректные символы для данного поля (разрешены только латинские символы)";
                    break;
            }
            code = "_NameFormat" + errorFormatCode;
            e.rejectValue(field + "." + PassengerInformationValidator.ERROR_CODES.IncorrectValue.name() + code, desc);
        }
    }

    private void addNumDocError(Errors e, int errorFormatCode) {
        if (errorFormatCode > 0) {
            String code = null;
            String desc = "";
            switch (errorFormatCode) {
                case 1:
                    desc = "Номер документа не соответствует формату: первые два символа должны быть буквы кириллицы";
                    break;
                case 2:
                    desc = "Номер документа не соответствует формату: с 3 по 9 символы должны быть цифры";
                    break;
                case 3:
                    desc = "Номер документа не соответствует формату: первые два символа должны быть римские цифры (латинскими буквами)";
                    break;
                case 4:
                    desc = "Номер документа не соответствует формату: слишком мало символов";
                    break;
                case 5:
                    desc = "Номер документа не соответствует формату: слишком много символов";
                    break;
                case 6:
                    desc = "Некорректные символы в номере документа";
                    break;
            }
            code = "_NumDocFormat" + errorFormatCode;
            e.rejectValue("documentNumber" + PassengerInformationValidator.ERROR_CODES.IncorrectValue.name() + code, desc);
        }
    }

}




