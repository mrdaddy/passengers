package com.rw.passengers.controllers;

import com.rw.passengers.dto.ErrorMessage;
import com.rw.passengers.dto.Passenger;
import com.rw.passengers.security.User;
import com.rw.passengers.services.PassengerService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad request", response = ErrorMessage.class, responseContainer = "List"),
        @ApiResponse(code = 503, message = "Service Unavailable"),
        @ApiResponse(code = 504, message = "Gateway Timeout")
})
@PreAuthorize("hasRole('U')")
@RestController
@Api(value="passengers", description="Сервис работы с пассажирами", tags = "Пассажиры", basePath="/passengers")
@RequestMapping(path = "/${service.version}/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
public class PassengerController {
    @Autowired
    PassengerService passengerService;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Создание пассажира", authorizations = @Authorization("jwt-auth"))
    @ResponseStatus( HttpStatus.CREATED )
    public Passenger createPassenger(@RequestBody @ApiParam Passenger passenger) {
        return passengerService.createPassenger(passenger);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{passengerId}")
    @ApiOperation(value = "Обновление пассажира по идентификатору", authorizations = @Authorization("jwt-auth"))
    @ResponseStatus( HttpStatus.ACCEPTED )
    public Passenger createPassenger(@ApiParam(required = true, value = "Уникальный идентификатор пассажира", example = "1") @PathVariable("passengerId") long passengerId, @RequestBody @ApiParam(required = true, value = "Данные пассажира") Passenger passenger) {
        return passengerService.updatePassenger(passengerId, passenger);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{passengerId}")
    @ApiOperation(value = "Удаление пассажира по идентификатору", authorizations = @Authorization("jwt-auth"))
    @ResponseStatus( HttpStatus.ACCEPTED)
    public void deletePassenger(@ApiParam(required = true, value = "Уникальный идентификатор пассажира", example = "1") @PathVariable("passengerId") long passengerId) {
        passengerService.deletePassenger(passengerId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{passengerId}")
    @ApiOperation(value = "Получение пассажира по идентификатору", authorizations = @Authorization("jwt-auth"))
    @ResponseStatus( HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK",
                    responseHeaders = {
                            @ResponseHeader(name = "ETag", response = String.class, description = "Хеш для кэширования")}),
            @ApiResponse(code = 304, message = "Not Modified")
    })
    public Passenger getPassenger(@ApiParam(required = true, value = "Уникальный идентификатор пассажира", example = "1") @PathVariable("passengerId") long passengerId, @RequestHeader(name="IF-NONE-MATCH", required = false) @ApiParam(name="IF-NONE-MATCH", value = "ETag из предыдущего закэшированного запроса") String inm) {
        return passengerService.getPassenger(passengerId);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Получение пассажиров пользователя", authorizations = @Authorization("jwt-auth"))
    @ResponseStatus( HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK",
                    responseHeaders = {
                            @ResponseHeader(name = "ETag", response = String.class, description = "Хеш для кэширования")}),
            @ApiResponse(code = 304, message = "Not Modified")
    })
    public List<Passenger> getPassengers(@RequestHeader(name="IF-NONE-MATCH", required = false) @ApiParam(name="IF-NONE-MATCH", value = "ETag из предыдущего закэшированного запроса") String inm, @RequestAttribute(value = "user", required = false) User user) {
        return passengerService.getPassengers(user);
    }

}
