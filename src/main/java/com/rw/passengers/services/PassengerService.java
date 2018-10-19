package com.rw.passengers.services;

import com.rw.passengers.dao.PassengerDao;
import com.rw.passengers.dto.Passenger;
import com.rw.passengers.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class PassengerService {

    @Autowired
    PassengerDao passengerDao;

    public Passenger createPassenger(@Valid  Passenger passenger, @Valid User user) {

        return passengerDao.createPassenger(passenger, user);
    }

    public Passenger updatePassenger(@Valid @Min(1) long passengerId, @Valid  Passenger passenger, @Valid User user) {
        return passengerDao.updatePassenger(passengerId, passenger, user);
    }

    public void deletePassenger(@Valid @Min(1) long passengerId, @Valid User user) {
        passengerDao.deletePassenger(passengerId, user);
    }

    public Passenger getPassenger(@Valid @Min(1) long passengerId, @Valid User user) {
        return passengerDao.getPassenger(passengerId, user);
    }

    public List<Passenger> getPassengers(@Valid User user) {
        return passengerDao.getPassengers(user);
    }

}
