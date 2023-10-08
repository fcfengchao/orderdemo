package com.example.demo;

import com.example.demo.model.Order;
import com.example.demo.request.CreateOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // Add any setup code you need here
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Prepare a request body with origin and destination coordinates
        CreateOrderRequest request = new CreateOrderRequest(new String[]{"START_LATITUDE", "START_LONGITUDE"}, new String[]{"END_LATITUDE", "END_LONGITUDE"});
        String requestBody = objectMapper.writeValueAsString(request);

        // Send a POST request to create an order
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Deserialize the response
        Order response = objectMapper.readValue(result.getResponse().getContentAsString(), Order.class);

        // Perform assertions on the response
        assertEquals("UNASSIGNED", response.getStatus());
        // Add more assertions as needed
    }

    @Test
    public void testTakeOrder() throws Exception {
        // Send a PATCH request to take an order by ID (replace 1 with an actual order ID)
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/orders/take/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Deserialize the response
        Order response = objectMapper.readValue(result.getResponse().getContentAsString(), Order.class);

        // Perform assertions on the response
        assertEquals("SUCCESS", response.getStatus());
        // Add more assertions as needed
    }

    @Test
    public void testListOrders() throws Exception {
        // Send a GET request to list orders with pagination (page and limit)
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/orders/list?page=1&limit=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Deserialize the response as an array of OrderResponse objects
        Order[] responses = objectMapper.readValue(result.getResponse().getContentAsString(), Order[].class);

        // Perform assertions on the response (e.g., check the list of orders)
        for (Order response : Objects.requireNonNull(responses)) {
            assertNotNull(response.getId()); // Check if the order has an ID
            assertNotNull(response.getDistance()); // Check if the order has a distance
            assertNotNull(response.getStatus()); // Check if the order has a status

            // You can add more specific assertions based on your application's logic
            // For example, you can check if the status is one of the expected values
            assertTrue(response.getStatus().equals("UNASSIGNED") || response.getStatus().equals("TAKEN"));

            // Add more assertions as needed based on your requirements
        }
    }
}

