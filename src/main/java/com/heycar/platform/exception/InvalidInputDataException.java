package com.heycar.platform.exception;

/**
 * <p>
 *  This exception is thrown in case of invalid input data to the API.
 * </p>
 *
 * @author Lalitkumar Kulkarni
 * @version 1.0
 * @since   01-06-2019
 */
public class InvalidInputDataException extends Exception {

    public InvalidInputDataException(String message){
        super(message);
    }

    public InvalidInputDataException(String message,Throwable throwable){
        super(message,throwable);
    }
}
