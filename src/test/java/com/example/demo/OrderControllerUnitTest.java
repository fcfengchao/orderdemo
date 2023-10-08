package com.example.demo;

import com.example.demo.controller.OrderController;
import com.example.demo.model.Order;
import com.example.demo.request.CreateOrderRequest;
import com.example.demo.service.OrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

public class OrderControllerUnitTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateOrder() {
        String[] origin = new String[]{"START_LATITUDE", "START_LONGITUDE"};
        String[] destination = new String[]{"END_LATITUDE", "END_LONGITUDE"};

        // Mock the behavior of the orderService.createOrder method with the string arrays
        Mockito.when(orderService.createOrder(Mockito.eq(origin), Mockito.eq(destination))).thenReturn(new Order());

        // Call the controller method with the string arrays
        ResponseEntity<Object> response = orderController.createOrder(new CreateOrderRequest(origin, destination));

        // Assert the response
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as needed

    }

    @Test
    public void testTakeOrder() {
        // Mock the behavior of the orderService.takeOrder method
        Mockito.when(orderService.takeOrder(Mockito.anyLong())).thenReturn(ResponseEntity.ok(new Order()));


        // Call the controller method
        ResponseEntity<Object> response = orderController.takeOrder(1L);

        // Assert the response
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testListOrders() {
        // Mock the behavior of the orderService.listOrders method
        Mockito.when(orderService.listOrders(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Arrays.asList(new Order()));

        // Call the controller method
        ResponseEntity<Object> response = orderController.listOrders("1", "10");

        // Assert the response
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as needed
    }
}

