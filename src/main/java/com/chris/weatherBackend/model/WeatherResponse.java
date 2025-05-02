package com.chris.weatherBackend.model;


import com.chris.weatherBackend.dto.Main;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class WeatherResponse {
    private String name;
    private Main main;
    private Weather[] weather;
    private Rain rain;
    private int cod; //200


    @Data
    public static class Main{
        private double temp;
        @JsonProperty("temp_min")
        private double tempMin;
        @JsonProperty("temp_max")
        private double tempMax;
        private int humidity;
    }

    @Data
    public static class Weather{
        private String main;
        private String description;
        private String icon;
    }

    @Data
    public static class Rain{
        @JsonProperty("1h")
        private double _1h; //rainfall
    }

}
