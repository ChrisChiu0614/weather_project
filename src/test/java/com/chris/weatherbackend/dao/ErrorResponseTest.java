package com.chris.weatherbackend.dao;

import com.chris.weatherbackend.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ErrorResponseTest {
    @Test
    void testErrorResponseGetter() {
        ErrorResponse err = new ErrorResponse("找不到資源", "Not Found", "404");
        assertEquals("404", err.getErrorCode());
        assertEquals("Not Found", err.getDetails());
        assertEquals("找不到資源", err.getMessage());
    }
}
