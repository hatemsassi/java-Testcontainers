package com.retailordersystem.controller;



import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailordersystem.model.Order;
import com.retailordersystem.repository.OrderRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
//@Disabled("Disabled for demonstration purposes")
public class OrderControllerIntegrationTest {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void shouldCreateOrderAndPublishEvent() throws Exception {
    	
        // Create a new order
        Order order = new Order("DUMMY_STATUS", "Order from Integration Test");

        // Perform POST request to create the order (using objectMapper)
        MvcResult mvcResult = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order))) 
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("DUMMY_STATUS")))
                .andReturn();
        // Extract the created order's ID from the response
        String responseContent = mvcResult.getResponse().getContentAsString();
        Long orderId = Long.parseLong(responseContent.substring(responseContent.indexOf("\"id\":") + 5, responseContent.indexOf(",")));

        // Verify order saved in the database
        Order savedOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(savedOrder.getStatus()).isEqualTo("DUMMY_STATUS");

        
    }
    
}