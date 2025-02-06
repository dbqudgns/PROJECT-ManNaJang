package com.culture.BAEUNDAY.exception;

import com.culture.BAEUNDAY.domain.review.ReviewController;
import com.culture.BAEUNDAY.domain.user.UserController;
import com.culture.BAEUNDAY.domain.user.DTO.response.ErrorResult;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = {UserController.class, ReviewController.class})
public class GlobalExceptionHandler {

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

    @ExceptionHandler({UsernameNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorResult> notFoundHandle(Exception ex) {

        ErrorResult errorResult = ErrorResult.builder()
                .status(400)
                .message(ex.getMessage())
                .errorCode("REQ_400")
                .build();

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResult> notMatchEntity(Exception ex) {

        ErrorResult errorResult = ErrorResult.builder()
                .status(403)
                .message(ex.getMessage())
                .errorCode("REQ_403")
                .build();

        return new ResponseEntity<>(errorResult, HttpStatus.FORBIDDEN);
    }

}
