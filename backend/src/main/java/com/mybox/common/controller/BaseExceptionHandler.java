package com.mybox.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybox.common.exception.BaseException;
import com.mybox.common.exception.BaseExceptionConstants;
import com.mybox.common.model.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice
public class BaseExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, Object>> baseExceptionHandler(BaseException e) {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(e.getHttpStatusType(), e.getHttpStatusCode(), e.getMessage());
        Map<String, Object> responseBody = objectMapper.convertValue(errorResponse, Map.class);

        return new ResponseEntity<>(responseBody, httpHeaders, httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validExceptionHandler(MethodArgumentNotValidException e) {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(
                BaseExceptionConstants.ExceptionClass.Validation.name(),
                httpStatus.value(),
                e.getBindingResult().getAllErrors().stream().findFirst().get().getDefaultMessage());

        Map<String, Object> responseBody = objectMapper.convertValue(errorResponse, Map.class);

        return new ResponseEntity<>(responseBody, httpHeaders, httpStatus);
    }
}
