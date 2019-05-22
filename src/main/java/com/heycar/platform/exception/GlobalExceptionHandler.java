package com.heycar.platform.exception;

import com.heycar.platform.model.ErrorDetails;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Set;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        final ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), "Input request Validation Failed",
                ex.getBindingResult().toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value= {DataIntegrityViolationException.class})
    public final ResponseEntity<ErrorDetails> validationException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value= {ConstraintViolationException.class})
    public final ResponseEntity<ErrorDetails> validationException(ConstraintViolationException ex, WebRequest request) {

        Set<ConstraintViolation<?>> constraintVio =  ex.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        constraintVio.stream().forEach(i->strBuilder.append(i.getMessageTemplate()).append(" & "));
        ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(),strBuilder.toString(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

}
