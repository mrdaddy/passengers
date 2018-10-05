package controllers;


import com.rw.passengers.controllers.PassengerController;
import com.rw.passengers.dto.Passenger;
import com.rw.passengers.security.User;
import com.rw.passengers.services.PassengerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class PassengerControllerTest {
    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private PassengerController passengerController;

    private List<Passenger> passengersExpected, passengersActual;
    private User user;
    long idForRequest;

    @Before
    public void setUp() {

        user = new User();
        passengersExpected = new ArrayList<>();

        long id = 0;
        Date date;
        date = java.sql.Date.valueOf("1990-01-30");
        idForRequest = 3;

        passengersExpected.add(new Passenger(++id, "Иван", "Иванович", "Иванов", Passenger.SEX_TYPE.M, date, "БЛР", "ПБ", "КН2014222"));
        passengersExpected.add(new Passenger(++id, "Петр", "Петрович", "Петров", Passenger.SEX_TYPE.M, date, "РУС", "МР", "КН5635432"));
        passengersExpected.add(new Passenger(++id, "Ирина", "Дмитриевна", "Крючкова", Passenger.SEX_TYPE.F, date, "UA", "МИ", "КН5667732"));

    }

    @Test
    public void getPassengers() {

        when(passengerService.getPassengers(user)).thenReturn(passengersExpected);
        passengersActual = passengerController.getPassengers("test", user);
        assertEquals(passengersExpected, passengersActual);
    }


    @Test
    public void getPassenger(){

        when(passengerService.getPassenger( idForRequest ,user)).thenReturn(passengersExpected.get(Math.toIntExact(idForRequest - 1)));
        Passenger passengerActual = passengerController.getPassenger(idForRequest,"test" , user);
        assertEquals(passengerActual, passengersExpected.get(Math.toIntExact(idForRequest - 1)));
    }

    @Test
    public void createPassenger() {

        when(passengerService.createPassenger(passengersExpected.get(0), user)).thenReturn(passengersExpected.get(0));
        Passenger passengerActual = passengerController.createPassenger(passengersExpected.get(0), user);
        assertEquals(passengerActual, passengersExpected.get(0));
    }


   @Test(expected=MockitoException.class)

    public void deletePassenger() throws MockitoException {

        doThrow(new IOException() ).when(passengerService).deletePassenger(idForRequest,user);
        passengerController.deletePassenger(idForRequest, user);

    }

    @Test
    public void updatePassenger() {

        when(passengerService.updatePassenger(idForRequest, passengersExpected.get(Math.toIntExact(idForRequest - 1)),user)).thenReturn(passengersExpected.get(0));
        Passenger passengerActual = passengerController.updatePassenger(idForRequest, passengersExpected.get(Math.toIntExact(idForRequest - 1)), user);
        assertEquals(passengerActual, passengersExpected.get(0));
    }

}
