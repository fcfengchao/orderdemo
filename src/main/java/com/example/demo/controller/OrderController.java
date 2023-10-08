package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.request.CreateOrderRequest;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create Order endpoint
    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            // Validate the request
            validateCoordinates(request.getOrigin(), "origin");
            validateCoordinates(request.getDestination(), "destination");

            // Create the order
            Order createdOrder = orderService.createOrder(request.getOrigin(), request.getDestination());

            // Return the response
            Map<String, Object> response = new HashMap<>();
            response.put("id", createdOrder.getId());
            response.put("distance", createdOrder.getDistance());
            response.put("status", "UNASSIGNED");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Invalid coordinates, return an error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // Helper method to validate coordinates
    private void validateCoordinates(String[] coordinates, String paramName) {
        if (coordinates == null || coordinates.length != 2) {
            String errorMessage = paramName + messageSource.getMessage("error.invalidCoordinates", null, LocaleContextHolder.getLocale());
            throw new IllegalArgumentException(errorMessage);
        }

        // Add further validation for latitude and longitude values if needed
    }

    // Take Order endpoint
    @PatchMapping("/take/{id}")
    public ResponseEntity<Object> takeOrder(@PathVariable Long id) {
        ResponseEntity<Object> response = orderService.takeOrder(id);
        return response;
    }

    // List Orders endpoint
    @GetMapping("/list")
    public ResponseEntity<Object> listOrders(@RequestParam String page, @RequestParam String limit) {
        try {
            int pageNumber = Integer.parseInt(page);
            int limitNumber = Integer.parseInt(limit);

            if (pageNumber < 1 || limitNumber <= 0) {
                // Invalid page or limit values, return an error response
                String errorMessage = messageSource.getMessage("error.invalidPageLimit", null, LocaleContextHolder.getLocale());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + errorMessage + "\"}");
            }

            List<Order> orders = orderService.listOrders(pageNumber, limitNumber);

            if (orders == null || orders.isEmpty()) {
                // No results, return an empty JSON array
                return ResponseEntity.ok(Collections.emptyList());
            } else {
                // Return the list of orders as JSON
                return ResponseEntity.ok(orders);
            }
        } catch (NumberFormatException e) {
            // Invalid page or limit values, return an error response
            String errorMessage = messageSource.getMessage("error.invalidPageLimit", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + errorMessage + "\"}");
        }
    }
}
