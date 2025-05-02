package com.chris.weatherBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5)); // The connection timeout is less than 5 seconds
        factory.setReadTimeout(Duration.ofSeconds(5));    // The read timeout is less than 5 seconds
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }
}
