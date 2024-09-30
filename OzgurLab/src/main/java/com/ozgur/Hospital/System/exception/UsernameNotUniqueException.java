package com.ozgur.Hospital.System.exception;

public class UsernameNotUniqueException extends RuntimeException{
    public UsernameNotUniqueException(String message) {
        super(message);
    }
}
