package com.rw.passengers.controllers;

import com.rw.passengers.dto.ErrorMessage;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad request", response = ErrorMessage.class, responseContainer = "List"),
        @ApiResponse(code = 503, message = "Service Unavailable"),
})
@PreAuthorize("hasRole('U')")
public class BaseController {
    public enum ERROR_PREFIX {validation, system, express}

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleInvalidRequest(ConstraintViolationException e, WebRequest request) {
        List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        ErrorMessage errorMessage = new ErrorMessage(ERROR_PREFIX.validation+".error",e.getLocalizedMessage());
        errors.add(errorMessage);
        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleInvalidRequest(MethodArgumentNotValidException e, WebRequest request) {
        List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        if(e.getBindingResult().hasErrors()) {
            for(ObjectError oe: e.getBindingResult().getAllErrors()) {
                ErrorMessage errorMessage = new ErrorMessage(oe.getCodes()[0],oe.getDefaultMessage());
                errors.add(errorMessage);
            }
        }
        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectException.class)
    protected List<ErrorMessage> handleConnectException(ConnectException e) {
        List<ErrorMessage> errors = new ArrayList<>();
        errors.add(new ErrorMessage(ERROR_PREFIX.system+".database_error", e.getMessage()));
        return errors;
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected List<ErrorMessage> handleDataAccessException(EmptyResultDataAccessException e) {
        List<ErrorMessage> errors = new ArrayList<>();
        errors.add(new ErrorMessage(ERROR_PREFIX.system+".database_error", e.getMessage()));
        return errors;
    }

    @ExceptionHandler(SQLException.class)
    protected List<ErrorMessage> handleSQLException(SQLException e) {
        List<ErrorMessage> errors = new ArrayList<>();
        errors.add(new ErrorMessage(ERROR_PREFIX.system+".database_error", e.getMessage()));
        return errors;
    }

}
