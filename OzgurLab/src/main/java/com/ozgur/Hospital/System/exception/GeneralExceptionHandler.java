package com.ozgur.Hospital.System.exception;

import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<?> adminNotFoundExceptionHandler(AdminNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotUniqueException.class)
    public ResponseEntity<?> UsernameNotUniqueExceptionHandler(UsernameNotUniqueException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException exception){
        return new ResponseEntity<>("There is a type problem with your parameter!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<?> missingParameterExceptionHandler(MissingParameterException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> UnauthorizedExceptionHandler(UnauthorizedException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

}
