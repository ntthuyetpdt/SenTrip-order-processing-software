package com.example.da_sentrip.handler;


import com.example.da_sentrip.config.MessageTemplate;
import com.example.da_sentrip.exception.BadRequestException;
import com.example.da_sentrip.exception.EntityValidationException;
import com.example.da_sentrip.exception.PartialUpdateException;
import com.example.da_sentrip.exception.ResourceNotFoundException;
import com.example.da_sentrip.model.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Date;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private MessageTemplate messageTemplate;

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFound(NoResourceFoundException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail = new ErrorDetail(new Date(), "Endpoint not found: " + e.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail = new ErrorDetail(new Date(), e.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityValidationException.class)
    public ResponseEntity<?> handleEntityValidationException(EntityValidationException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail = new ErrorDetail(new Date(), e.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PartialUpdateException.class)
    public ResponseEntity<?> handlePartialUpdateException(PartialUpdateException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail = new ErrorDetail(new Date(), messageTemplate.message("error.validation"), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail = new ErrorDetail(new Date(), messageTemplate.message("error.validation"), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail = new ErrorDetail(new Date(), e.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail = new ErrorDetail(new Date(), e.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail = new ErrorDetail(new Date(), e.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail = new ErrorDetail(new Date(), messageTemplate.message("error.system"), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}