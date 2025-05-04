package com.chris.weatherbackend.dto;

import lombok.Data;

@Data
public class WeatherDTO {
    private String city;
    private double temperature;
    private double temperatureMin;
    private double temperatureMax;
    private String description;
    private String icon;
    private double rainfall;
    private String umbrellaAdvice;
    private String clothingAdvice;


}
