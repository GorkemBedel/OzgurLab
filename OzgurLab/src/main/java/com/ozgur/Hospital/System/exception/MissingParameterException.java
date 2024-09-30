package com.ozgur.Hospital.System.exception;

public class MissingParameterException extends RuntimeException{
    public MissingParameterException(String message) {
        super(message);
    }
}
