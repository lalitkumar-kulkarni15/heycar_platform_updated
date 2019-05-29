package com.heycar.platform.exception;

public class ListingProcessingException extends Exception {

    public ListingProcessingException(String message){
        super(message);
    }

    public ListingProcessingException(String message,Throwable throwable){
        super(message,throwable);
    }
}
