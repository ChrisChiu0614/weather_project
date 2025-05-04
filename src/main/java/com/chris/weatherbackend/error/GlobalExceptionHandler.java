package com.chris.weatherbackend.error;

import com.chris.weatherbackend.dto.ErrorResponse;
import com.chris.weatherbackend.error.custom.CityNotFoundException;
import com.chris.weatherbackend.error.custom.ExternalApiException;
import com.chris.weatherbackend.error.custom.InvalidParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(CityNotFoundException e) {
        ErrorResponse error = new ErrorResponse("城市不存在", e.getMessage(), "NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParameterException(InvalidParameterException e) {
        ErrorResponse error = new ErrorResponse("無效參數", e.getMessage(), "INVALID_PARAMETER");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ErrorResponse> handleExternalApiException(ExternalApiException e) {
        ErrorResponse error = new ErrorResponse("外部API錯誤", e.getMessage(), "API_ERROR");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        ErrorResponse error = new ErrorResponse("伺服器內部錯誤", e.getMessage(), "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
