package com.integration.service.controller;

import com.integration.service.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private static final String ORDER_CREATE_MSG = "Order has been created";

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @DisplayName("Test create order")
    @Test
    public void createOrder() {
        when(orderService.createOrder()).thenReturn(ORDER_CREATE_MSG);
        ResponseEntity<String> actualResponse = orderController.createOrder();
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(ORDER_CREATE_MSG);
    }
}
