package com.chris.weatherbackend.service;

import com.chris.weatherbackend.dto.WeatherDTO;
import com.chris.weatherbackend.error.custom.CityNotFoundException;
import com.chris.weatherbackend.error.custom.ExternalApiException;
import com.chris.weatherbackend.model.WeatherResponse;
import com.chris.weatherbackend.model.WeatherResponse.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherService weatherService;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService(restTemplate);

    }

    private WeatherResponse getMockResponse(){
        WeatherResponse response = new WeatherResponse();

        response.setName("Taipei");

        WeatherResponse.Main main = new WeatherResponse.Main();
        main.setTemp(11.2);
        response.setMain(main);

        WeatherResponse.Weather weather = new WeatherResponse.Weather();
        weather.setDescription("clear sky");
        response.setWeather(new Weather[]{weather});

        WeatherResponse.Rain rain = new WeatherResponse.Rain();
        rain.set_1h(0.5);
        response.setRain(rain);
        response.setCod(200);

        return response;
    }

    @Test
    void testGetWeatherSuccess(){
        WeatherResponse response = getMockResponse();

        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(response);

        WeatherDTO result = weatherService.getWeather("Taipei");

        assertEquals("Taipei", result.getCity());
        assertEquals(11.2, result.getTemperature());
        assertEquals("clear sky", result.getDescription());
        assertEquals(0.5, result.getRainfall());
        assertEquals("建議帶傘，預計有降雨！", result.getUmbrellaAdvice());
        assertEquals("天氣較冷，建議穿外套或毛衣。", result.getClothingAdvice());

    }

    @Test
    void testGetWeatherThrowException404(){
        WeatherResponse response = getMockResponse();
        response.setCod(404);
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(response);

        assertThrows(ExternalApiException.class,()->{
            weatherService.getWeather("Taipei");
        });

    }

    @Test
    void testGetWeatherThrowCityNotFoundException(){
        HttpClientErrorException httpClientErrorException = new HttpClientErrorException(
                HttpStatus.NOT_FOUND,
                "not found",
                null,
                null,
                null
        );

        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenThrow(httpClientErrorException);

        assertThrows(CityNotFoundException.class,()->{
            weatherService.getWeather("Taipei");
        });

    }

    @Test
    void testGetWeatherThrowCityExternalApiException(){
        HttpClientErrorException httpClientErrorException = new HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "API request failed",
                null,
                null,
                null
        );

        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenThrow(httpClientErrorException);

        assertThrows(ExternalApiException.class,()->{
            weatherService.getWeather("Taipei");
        });

    }

    @Test
    void testRainfallAdviceWillRaining(){
        WeatherResponse response = getMockResponse();
        response.getRain().set_1h(0.5);
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(response);
        WeatherDTO result = weatherService.getWeather("Taipei");
        assertEquals("建議帶傘，預計有降雨！", result.getUmbrellaAdvice());
    }

    @Test
    void testRainfallAdviceWontRaining(){
        WeatherResponse response = getMockResponse();
        response.getRain().set_1h(0.1);
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(response);
        WeatherDTO result = weatherService.getWeather("Taipei");
        assertEquals("今天應該不用帶傘。", result.getUmbrellaAdvice());
    }

    @Test
    void testClothingAdviceWillBeCold(){
        WeatherResponse response = getMockResponse();
        response.getMain().setTemp(14);
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(response);
        WeatherDTO result = weatherService.getWeather("Taipei");
        assertEquals("天氣較冷，建議穿外套或毛衣。", result.getClothingAdvice());
    }

    @Test
    void testClothingAdviceWillBeWarm(){
        WeatherResponse response = getMockResponse();
        response.getMain().setTemp(24);
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(response);
        WeatherDTO result = weatherService.getWeather("Taipei");
        assertEquals("天氣舒適，穿長袖或薄外套即可。", result.getClothingAdvice());
    }

    @Test
    void testClothingAdviceWillBeHot(){
        WeatherResponse response = getMockResponse();
        response.getMain().setTemp(30);
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(response);
        WeatherDTO result = weatherService.getWeather("Taipei");
        assertEquals("天氣炎熱，穿短袖清涼一點！", result.getClothingAdvice());
    }

}
