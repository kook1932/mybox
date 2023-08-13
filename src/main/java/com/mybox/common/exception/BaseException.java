package com.mybox.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    private final BaseExceptionConstants.ExceptionClass exceptionClass;
    private final HttpStatus httpStatus;

    public BaseException(BaseExceptionConstants.ExceptionClass exceptionClass, HttpStatus httpStatus, String message) {
        super(message);
        this.exceptionClass = exceptionClass;
        this.httpStatus = httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

    public String getHttpStatusType() {
        return httpStatus.getReasonPhrase();
    }
}
