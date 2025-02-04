package com.culture.BAEUNDAY.exception;

import com.culture.BAEUNDAY.controller.UserController;
import com.culture.BAEUNDAY.dto.response.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = UserController.class)
public class UserExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions (MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> notEnoughData(IllegalArgumentException ex) {

        ErrorResult errorResult = ErrorResult.builder()
                .status(400)
                .message(ex.getMessage())
                .errorCode("REQ_400")
                .build();

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResult> userHandle(UsernameNotFoundException ex) {

        ErrorResult errorResult = ErrorResult.builder()
                .status(400)
                .message(ex.getMessage())
                .errorCode("REQ_400")
                .build();

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

}
