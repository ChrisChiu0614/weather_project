package com.chris.weatherBackend.service;

import com.chris.weatherBackend.dto.WeatherDTO;
import com.chris.weatherBackend.error.custom.CityNotFoundException;
import com.chris.weatherBackend.error.custom.ExternalApiException;

import com.chris.weatherBackend.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }


    //取得天氣資訊 by city
    public WeatherDTO getWeather(String city) {
        try {
            String url = String.format("%s?q=%s&appid=%s&units=metric&lang=zh_tw", apiUrl, city, apiKey);

            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

            if (response == null || response.getWeather() == null || response.getCod() != 200) {
                throw new RuntimeException("無法獲取天氣數據");
            }

            return getWeatherDTO(response);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                throw new CityNotFoundException("City not found: " + city);
            }
            throw new ExternalApiException("API request failed: " + e.getMessage());

        }
    }

    private WeatherDTO getWeatherDTO(WeatherResponse response) {
        WeatherDTO dto = new WeatherDTO();
        dto.setCity(response.getName());
        dto.setTemperature(response.getMain().getTemp());
        dto.setTemperatureMin(response.getMain().getTempMin());
        dto.setTemperatureMax(response.getMain().getTempMax());
        dto.setDescription(response.getWeather()[0].getDescription());
        dto.setIcon(response.getWeather()[0].getIcon());
        dto.setRainfall(response.getRain() != null ? response.getRain().get_1h() : 0);
        dto.setUmbrellaAdvice(getUmbrellaAdvice(dto.getRainfall()));
        dto.setClothingAdvice(getClothingAdvice(dto.getTemperature()));
        return dto;
    }

    private String getUmbrellaAdvice(double rainfall){
        return rainfall > 0.3 ? "建議帶傘，預計有降雨！" : "今天應該不用帶傘。";
    }

    private String getClothingAdvice(double temp){
        if (temp < 15) {
            return "天氣較冷，建議穿外套或毛衣。";
        } else if (temp < 25) {
            return "天氣舒適，穿長袖或薄外套即可。";
        } else {
            return "天氣炎熱，穿短袖清涼一點！";
        }
    }
}
