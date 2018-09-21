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
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@Api(value="passengers", description="Сервис работы с пассажирами", tags = "Пассажиры", basePath="/passengers")
@RequestMapping(path = "/${service.version}/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
public class PassengerController extends BaseController {
    @Autowired
    PassengerService passengerService;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Создание пассажира", authorizations = @Authorization("jwt-auth"))
    @ResponseStatus( HttpStatus.CREATED )
    public Passenger createPassenger(@RequestBody @ApiParam Passenger passenger, @ApiIgnore @RequestAttribute(value = "user", required = false) User user) {
        return passengerService.createPassenger(passenger, user);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{passengerId}")
    @ApiOperation(value = "Обновление пассажира по идентификатору", authorizations = @Authorization("jwt-auth"))
    @ResponseStatus( HttpStatus.ACCEPTED )
    public Passenger createPassenger(@ApiParam(required = true, value = "Уникальный идентификатор пассажира", example = "1") @PathVariable("passengerId") long passengerId, @RequestBody @ApiParam(required = true, value = "Данные пассажира") Passenger passenger, @ApiIgnore @RequestAttribute(value = "user", required = false) User user) {
        return passengerService.updatePassenger(passengerId, passenger, user);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{passengerId}")
    @ApiOperation(value = "Удаление пассажира по идентификатору", authorizations = @Authorization("jwt-auth"))
    @ResponseStatus( HttpStatus.ACCEPTED)
    public void deletePassenger(@ApiParam(required = true, value = "Уникальный идентификатор пассажира", example = "1") @PathVariable("passengerId") long passengerId, @ApiIgnore @RequestAttribute(value = "user", required = false) User user) {
        passengerService.deletePassenger(passengerId, user);
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
    public Passenger getPassenger(@ApiParam(required = true, value = "Уникальный идентификатор пассажира", example = "1") @PathVariable("passengerId") long passengerId, @RequestHeader(name="IF-NONE-MATCH", required = false) @ApiParam(name="IF-NONE-MATCH", value = "ETag из предыдущего закэшированного запроса") String inm, @ApiIgnore @RequestAttribute(value = "user", required = false) User user) {
        return passengerService.getPassenger(passengerId, user);
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
    public List<Passenger> getPassengers(@RequestHeader(name="IF-NONE-MATCH", required = false) @ApiParam(name="IF-NONE-MATCH", value = "ETag из предыдущего закэшированного запроса") String inm, @ApiIgnore @RequestAttribute(value = "user", required = false) User user) {
        return passengerService.getPassengers(user);
    }

}
