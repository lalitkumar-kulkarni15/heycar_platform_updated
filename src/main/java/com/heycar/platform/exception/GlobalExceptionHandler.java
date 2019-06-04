package com.heycar.platform.exception;

import com.heycar.platform.model.ErrorDetails;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * <p>
 *  This class is the global exception handler. This is responsible to handle specific exceptions and
 *  throw back appropriate http status codes with error messages.
 * </p>
 *
 * @author  Lalitkumar Kulkarni
 * @version 1.0
 * @since   01-06-2019
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    /**
     * <p>
     *  This method handles the constraint violation exception which is thrown by the controller layer.
     *  This throws a 400 BAD_REQUEST http status code to the consumer if the constraint is violated.
     * </p>
     *
     * @param  ex      {@link ConstraintViolationException
     * @param  request {@link WebRequest}
     * @return         {@link ResponseEntity}
     */
    @ExceptionHandler(value= {ConstraintViolationException.class})
    public final ResponseEntity<ErrorDetails> handleConstrntViolationException(ConstraintViolationException ex, WebRequest request) {

        Set<ConstraintViolation<?>> constraintVio =  ex.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        constraintVio.stream().forEach(i->strBuilder.append(i.getMessageTemplate()).append(" - "));
        ErrorDetails errorDetails = new ErrorDetails(strBuilder.toString(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * <p>
     *  This method handles the validation exception {@link HttpMessageNotReadableException} and {@link HttpMessageNotWritableException}
     *  which is thrown from the controller layer.This throws 400 BAD_REQUEST http status code along with the appropriate error message
     *  to the consumer of the service.
     * </p>
     *
     * @param ex      {@link Exception}
     * @param request {@link WebRequest}
     * @return        {@link ResponseEntity}
     */
    @ExceptionHandler(value= {HttpMessageNotReadableException.class,HttpMessageNotWritableException.class})
    public final ResponseEntity<ErrorDetails> handleValidationException(Exception ex,WebRequest request) {

        if(ex.getCause() instanceof CsvRequiredFieldEmptyException){
            CsvRequiredFieldEmptyException csvExp = (CsvRequiredFieldEmptyException) ex.getCause();
            String exceptionDtls = csvExp.getMessage().concat(" ").concat(" at line number "+csvExp.getLineNumber()+ " in the csv file.");
            ErrorDetails errorDetails = new ErrorDetails(exceptionDtls, request.getDescription(false));
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        } else {
            ErrorDetails errorDetails = new ErrorDetails(ex.getCause().getMessage(), request.getDescription(false));
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * <p>
     * This method handles the internal technical exception thrown by the lower layers.
     * It returns back 500 INTERNAL_TECHNICAL_EXCEPTION http status code back to the
     * consumer of the API.
     *
     * </p>
     * @param ex      {@link Exception}
     * @param request {@link WebRequest}
     * @return        {@link ResponseEntity}
     */
    @ExceptionHandler(value= {Exception.class})
    public final ResponseEntity<ErrorDetails> handleInternalTechException(Exception ex, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
