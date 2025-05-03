package com.chris.weatherBackend.service;

import com.chris.weatherBackend.dto.WeatherDTO;
import com.chris.weatherBackend.error.custom.CityNotFoundException;
import com.chris.weatherBackend.error.custom.ExternalApiException;
import com.chris.weatherBackend.model.WeatherResponse;
import com.chris.weatherBackend.model.WeatherResponse.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {
    private WeatherService weatherService;
    private RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        weatherService = new WeatherService(restTemplate);

    }

    @Test
    void testGetWeather_Success() {
        WeatherResponse mockResponse = new WeatherResponse();
        mockResponse.setCod(200);
        mockResponse.setName("Taipei");

        WeatherResponse.Main main = new WeatherResponse.Main();
        main.setTemp(28);
        main.setTempMin(25);
        main.setTempMax(30);
        mockResponse.setMain(main);

        WeatherResponse.Weather weather = new WeatherResponse.Weather();
        weather.setDescription("晴天");
        weather.setIcon("01d");
        mockResponse.setWeather(new WeatherResponse.Weather[]{weather});

        WeatherResponse.Rain rain = new WeatherResponse.Rain();
        rain.set_1h(0.5);
        mockResponse.setRain(rain);

        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(mockResponse);

        WeatherDTO dto = weatherService.getWeather("Taipei");

        assertEquals("Taipei", dto.getCity());
        assertEquals(28, dto.getTemperature());
        assertEquals("晴天", dto.getDescription());
        assertEquals("01d", dto.getIcon());
        assertEquals(0.5, dto.getRainfall());
        assertEquals("建議帶傘，預計有降雨！", dto.getUmbrellaAdvice());
        assertEquals("天氣炎熱，穿短袖清涼一點！", dto.getClothingAdvice());
    }

    @Test
    void testGetWeather_CityNotFound() {
        HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", null, null, null);
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenThrow(exception);

        assertThrows(CityNotFoundException.class, () -> weatherService.getWeather("UnknownCity"));
    }

    @Test
    void testGetWeather_OtherHttpError() {
        HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, "Error", null, null, null);
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenThrow(exception);

        assertThrows(ExternalApiException.class, () -> weatherService.getWeather("SomeCity"));
    }

    @Test
    void testGetWeather_NullResponse() {
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(null);
        assertThrows(RuntimeException.class, () -> weatherService.getWeather("Taipei"));
    }

    @Test
    void testGetWeather_NullWeatherArray() {
        WeatherResponse mockResponse = new WeatherResponse();
        mockResponse.setCod(200);
        mockResponse.setMain(new WeatherResponse.Main());
        mockResponse.setName("Taipei");
        mockResponse.setWeather(null);
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(mockResponse);

        assertThrows(RuntimeException.class, () -> weatherService.getWeather("Taipei"));
    }

    @Test
    void testGetWeather_WrongCod() {
        WeatherResponse mockResponse = new WeatherResponse();
        mockResponse.setCod(500);
        mockResponse.setMain(new WeatherResponse.Main());
        mockResponse.setWeather(new Weather[]{new Weather()});
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(mockResponse);

        assertThrows(RuntimeException.class, () -> weatherService.getWeather("Taipei"));
    }

}
