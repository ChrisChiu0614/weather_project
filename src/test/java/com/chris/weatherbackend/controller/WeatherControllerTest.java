package com.chris.weatherbackend.controller;


import com.chris.weatherbackend.dto.WeatherDTO;
import com.chris.weatherbackend.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {
    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    @Test
    void shouldReturnWeatherInfoForGivenCity() throws Exception {
        // Arrange
        WeatherDTO mockDTO = new WeatherDTO();
        mockDTO.setCity("Taipei");
        mockDTO.setTemperature(28.5);
        mockDTO.setDescription("晴時多雲");
        mockDTO.setRainfall(0.2);
        mockDTO.setUmbrellaAdvice("今天應該不用帶傘。");
        mockDTO.setClothingAdvice("天氣炎熱，穿短袖清涼一點！");

        when(weatherService.getWeather(anyString())).thenReturn(mockDTO);

        // Act & Assert
        mockMvc.perform(get("/api/weather")
                        .param("city", "Taipei")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Taipei"))
                .andExpect(jsonPath("$.temperature").value(28.5))
                .andExpect(jsonPath("$.description").value("晴時多雲"))
                .andExpect(jsonPath("$.rainfall").value(0.2))
                .andExpect(jsonPath("$.umbrellaAdvice").value("今天應該不用帶傘。"))
                .andExpect(jsonPath("$.clothingAdvice").value("天氣炎熱，穿短袖清涼一點！"));
    }
}
