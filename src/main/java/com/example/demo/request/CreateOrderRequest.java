package com.example.demo.request;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String[] origin;
    private String[] destination;

    public CreateOrderRequest(String[] origin, String[] destination) {
        this.origin = origin;
        this.destination = destination;
    }
}
