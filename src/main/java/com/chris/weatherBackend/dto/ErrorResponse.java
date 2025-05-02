package com.chris.weatherBackend.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private String details;
    private String errorCode;

    public ErrorResponse(String errorCode, String details, String message) {
        this.errorCode = errorCode;
        this.details = details;
        this.message = message;
    }
}
