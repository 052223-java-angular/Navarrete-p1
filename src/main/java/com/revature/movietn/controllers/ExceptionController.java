package com.revature.movietn.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.revature.movietn.utils.custom_exceptions.ResourceConflictException;
import com.revature.movietn.utils.custom_exceptions.RoleNotFoundException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> roleNotFoundExceptionHandler(RoleNotFoundException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("timestamp", new Date(System.currentTimeMillis()));
        resBody.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(resBody);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> resourceConflictExceptionHandler(ResourceConflictException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("timestamp", new Date(System.currentTimeMillis()));
        resBody.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resBody);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException e) {
        Map<String, Object> resBody = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((fe) -> {
            String fieldName = ((FieldError) fe).getField();
            String errorMessage = fe.getDefaultMessage();
            resBody.put(fieldName, errorMessage);
        });
        resBody.put("timestamp", new Date(System.currentTimeMillis()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody);
    }
}