package com.heycar.platform.exception;

public class CsvValidationException extends Exception {

    public CsvValidationException(String exMessage){
        super(exMessage);
    }

    public CsvValidationException(Exception throwable){
        super(throwable);
    }
}
