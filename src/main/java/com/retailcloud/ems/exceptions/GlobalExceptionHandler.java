package com.retailcloud.ems.exceptions;

import com.retailcloud.ems.payload.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFound(
            ResourceNotFoundException ex,
            WebRequest request
    ){
        ErrorDto error = new ErrorDto();
        error.setDate(new Date());
        error.setMessage("Something went wrong: " + ex.getMessage());
        error.setRequest(request.getDescription(true));

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
