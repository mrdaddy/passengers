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
        return passenger;
    }

    public Passenger updatePassenger(@Valid @Min(1) long passengerId, @Valid  Passenger passenger, @Valid User user) {
        return passenger;
    }

    public void deletePassenger(@Valid @Min(1) long passengerId, @Valid User user) {
    }

    public Passenger getPassenger(@Valid @Min(1) long passengerId, @Valid User user) {
        return new Passenger();
    }

    public List<Passenger> getPassengers(@Valid User user) {
        return new ArrayList<Passenger>();
    }
}
