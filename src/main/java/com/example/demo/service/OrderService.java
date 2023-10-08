package com.example.demo.service;

import com.example.demo.model.Order;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    Order createOrder(String[] origin, String[] destination);

    ResponseEntity<Object> takeOrder(Long id);

    List<Order> listOrders(int page, int limit);
}