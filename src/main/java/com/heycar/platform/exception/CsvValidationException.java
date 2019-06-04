package com.heycar.platform.exception;

/**
 * <p>
 * This is a custom exception class which is used in case there is a validation
 * exception with respect to CSV data validation.
 * </p>
 *
 * @since 01-06-2019
 * @author  Lalitkumar Kulkarni
 * @version 1.0
 */
public class CsvValidationException extends Exception {

    public CsvValidationException(String exMessage){
        super(exMessage);
    }

    public CsvValidationException(Exception throwable){
        super(throwable);
    }
}
