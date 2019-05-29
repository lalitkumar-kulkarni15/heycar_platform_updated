package com.heycar.platform.exception;

public class InvalidInputDataException extends Exception {

    public InvalidInputDataException(String message){
        super(message);
    }

    public InvalidInputDataException(String message,Throwable throwable){
        super(message,throwable);
    }
}
