package com.rcr.customException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetails> sellerExceptionHandler(SellerException se, WebRequest request){
        ErrorDetails error = new ErrorDetails();
        error.setError(se.getMessage());
        error.setDetails(request.getDescription(false));
        error.setTimeStamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetails> productExceptionHandler(ProductException se, WebRequest request){
        ErrorDetails error = new ErrorDetails();
        error.setError(se.getMessage());
        error.setDetails(request.getDescription(false));
        error.setTimeStamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
