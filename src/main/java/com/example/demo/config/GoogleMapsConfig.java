package com.example.demo.config;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleMapsConfig {
    @Value("${google.maps.api.key}") // Add your API key in the application.properties file
    private String apiKey;

    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder().apiKey(apiKey).build();
    }
}
