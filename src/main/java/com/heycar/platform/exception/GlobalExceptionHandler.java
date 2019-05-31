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

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value= {ConstraintViolationException.class})
    public final ResponseEntity<ErrorDetails> handleConstrntViolationException(ConstraintViolationException ex, WebRequest request) {

        Set<ConstraintViolation<?>> constraintVio =  ex.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        constraintVio.stream().forEach(i->strBuilder.append(i.getMessageTemplate()).append(" - "));
        ErrorDetails errorDetails = new ErrorDetails(strBuilder.toString(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

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

    @ExceptionHandler(value= {Exception.class})
    public final ResponseEntity<ErrorDetails> handleInternalTechException(Exception ex, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
