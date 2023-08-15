package com.mybox.common.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class ErrorResponse {

    private String errorType;
    private int code;
    private String message;

}
