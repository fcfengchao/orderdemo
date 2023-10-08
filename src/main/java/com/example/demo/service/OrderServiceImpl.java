package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final GeoApiContext geoApiContext;

    @Autowired
    private MessageSource messageSource;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, GeoApiContext geoApiContext) {
        this.orderRepository = orderRepository;
        this.geoApiContext = geoApiContext;
    }

    @Override
    public Order createOrder(String[] origin, String[] destination) {
        // Create a new Order instance
        Order order = new Order();

        // Calculate the distance and set it in the order
        double distance = calculateDistance(origin, destination);
        order.setDistance(distance);
        order.setStatus("UNASSIGNED");

        // Save the order in the database
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> takeOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findOrderForUpdate(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            // Check if the order is already taken
            if ("TAKEN".equals(order.getStatus())) {
                // Order has already been taken, return a conflict response
                String errorMessage = messageSource.getMessage("error.orderAlreadyTaken", null, LocaleContextHolder.getLocale());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("{\"error\": \"" + errorMessage + "\"}");
            } else {
                // Mark the order as taken
                order.setStatus("TAKEN");
                orderRepository.save(order);

                // Commit the transaction
                return ResponseEntity.ok("{\"status\": \"SUCCESS\"}");
            }
        } else {
            // Order not found, return a not found response
            String errorMessage = messageSource.getMessage("error.orderNotFound", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + errorMessage + "\"}");
        }
    }

    @Override
    public List<Order> listOrders(int page, int limit) {
        // Create a Pageable object for pagination
        Pageable pageable = PageRequest.of(page - 1, limit);

        // Retrieve a page of orders
        Page<Order> orderPage = orderRepository.findAll(pageable);

        // Extract the list of orders from the page
        List<Order> orders = orderPage.getContent();

        return orders;
    }

    private double calculateDistance(String[] origin, String[] destination) {
        try {
            DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(geoApiContext)
                    .origins(origin)
                    .destinations(destination)
                    .mode(TravelMode.DRIVING) // You can change the travel mode as needed
                    .units(Unit.METRIC)
                    .await();

            if (distanceMatrix.rows.length > 0 && distanceMatrix.rows[0].elements.length > 0) {
                return distanceMatrix.rows[0].elements[0].distance.inMeters;
            }
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return 0; // Handle the error case
    }
}
