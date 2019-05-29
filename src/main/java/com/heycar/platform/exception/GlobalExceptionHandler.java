package com.heycar.platform.exception;

import com.heycar.platform.model.ErrorDetails;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.time.LocalDate;
import java.util.Set;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {


   /* protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        final ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), "Input request Validation Failed",
                ex.getBindingResult().toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }*/

    @ExceptionHandler(value= {DataIntegrityViolationException.class})
    public final ResponseEntity<ErrorDetails> validationException(Throwable ex, WebRequest request) {
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

   /*
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {

        ErrorDetails errorDetails = null;

            if(ex instanceof HttpMessageNotReadableException){

                HttpMessageNotReadableException httpEx = (HttpMessageNotReadableException) ex;
                Throwable csvEx = httpEx.getCause();
                if(csvEx instanceof CsvRequiredFieldEmptyException){

                    CsvRequiredFieldEmptyException thx = (CsvRequiredFieldEmptyException) csvEx;

                    long lineNo = thx.getLineNumber();
                    String mesage = thx.getMessage();
                    String completeMsg = mesage.concat(" at line number "+lineNo);
                    errorDetails = new ErrorDetails(LocalDate.now(),completeMsg,null);
                }
            }
//            if(ex.getCause() instanceof CsvRequiredFieldEmptyException){
////                CsvRequiredFieldEmptyException csvReqd = (CsvRequiredFieldEmptyException) ex.getCause();
////                long lineNo = csvReqd.getLineNumber();
////                String message = csvReqd.getMessage();
////            }
////            HttpMessageNotReadableException httpMessage = (HttpMessageNotReadableException) ex;
////            httpMessage.getCause().getMessage();



        return new ResponseEntity(errorDetails, headers, status);
    }*/

    @ExceptionHandler(value= {HttpMessageNotReadableException.class})
    public final ResponseEntity<ErrorDetails> validationException(WebRequest request,HttpMessageNotReadableException ex) {

        System.out.println("This was called.");
        if(ex.getCause() instanceof CsvRequiredFieldEmptyException){

            CsvRequiredFieldEmptyException csvExp = (CsvRequiredFieldEmptyException) ex.getCause();
            String exceptionDtls = csvExp.getMessage().concat(" ").concat(" at line number "+csvExp.getLineNumber()+ " in the csv file.");
            ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(),exceptionDtls, request.getDescription(false));
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value= {HttpMessageNotWritableException.class})
    public final ResponseEntity<ErrorDetails> validationException(HttpMessageNotWritableException ex, WebRequest request) {

        System.out.println("This was called.");
        if(ex.getCause() instanceof CsvRequiredFieldEmptyException){

            CsvRequiredFieldEmptyException csvExp = (CsvRequiredFieldEmptyException) ex.getCause();
            String exceptionDtls = csvExp.getMessage().concat(" ").concat(" at line number "+csvExp.getLineNumber()+ " in the csv filw.");
            ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(),exceptionDtls, request.getDescription(false));
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    /*@ExceptionHandler(value= {RuntimeException.class})
    public final ResponseEntity<ErrorDetails> validationException(RuntimeException ex, WebRequest request) {

        System.out.println("This was called.");
        if(ex.getCause() instanceof CsvRequiredFieldEmptyException){

            CsvRequiredFieldEmptyException csvExp = (CsvRequiredFieldEmptyException) ex.getCause();
            String exceptionDtls = csvExp.getMessage().concat(" ").concat(" at line number "+csvExp.getLineNumber()+ " in the csv filw.");
            ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(),exceptionDtls, request.getDescription(false));
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }*/

}
