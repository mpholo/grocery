package com.mpholo.project.grocery.controller.api.v1;

import com.mpholo.project.grocery.service.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> hanndledNptFoundException(Exception exception, WebRequest request) {
        return  new ResponseEntity<Object>( "Resource Not Found",new HttpHeaders(), HttpStatus.NOT_FOUND);

    }
}
