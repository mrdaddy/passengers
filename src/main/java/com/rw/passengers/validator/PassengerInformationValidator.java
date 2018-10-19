package com.rw.passengers.validator;

import com.rw.passengers.dto.Passenger;
import com.rw.passengers.services.ValidationService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

@Component
@NoArgsConstructor
public class PassengerInformationValidator implements Validator {


    public enum ERROR_CODES {NotFound, IncorrectValue, IncompatibleValue, Denied};

    @Autowired
    private ValidationService validationService;

        @Override
        public boolean supports(Class<?> clazz) {
            return Passenger.class.equals(clazz);
        }

        @Override
        public void validate(Object target, Errors e) {

            if (target instanceof Passenger) {

                Passenger passenger = (Passenger) target;

                validationService.validatePassengerInformation(passenger, e);
                validationService.validateBirthday(passenger.getBirthday(), e);
            }
        }
}

