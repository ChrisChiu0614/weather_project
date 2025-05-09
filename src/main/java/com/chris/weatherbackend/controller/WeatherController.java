package com.chris.weatherbackend.controller;

import com.chris.weatherbackend.dto.WeatherDTO;
import com.chris.weatherbackend.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<WeatherDTO> getWeather(@RequestParam(defaultValue = "Taipei") String city) {
        WeatherDTO weather = weatherService.getWeather(city);

        return ResponseEntity.ok(weather);
    }
}
