package com.heycar.platform.exception;

/**
 * <p>
 *  This exception is thrown when there is any internal technical failure.
 * </p>
 *
 * @author  Lalitkumar Kulkarni
 * @since   02-06-2019
 * @version 1.0
 */
public class ListingProcessingException extends Exception {

    public ListingProcessingException(String message){
        super(message);
    }

    public ListingProcessingException(String message,Throwable throwable){
        super(message,throwable);
    }
}
