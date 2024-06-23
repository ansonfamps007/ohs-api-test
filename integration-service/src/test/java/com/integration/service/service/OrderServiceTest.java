package com.integration.service.service;

import com.integration.Order;
import com.integration.OrderServiceGrpc;
import com.integration.service.service.impl.OrderServiceImpl;
import com.integration.service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations="classpath:application-test.yml")
public class OrderServiceTest {
    @Mock
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    @InjectMocks
    private OrderServiceImpl orderService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(userService) {
            public String createOrder(String userId, String productId, int quantity) {
                Order.CreateOrderRequest request = Order.CreateOrderRequest.newBuilder()
                        .setUserPid(userId).addAllProducts(List.of(Order.Product.newBuilder()
                                .setPid(productId).build()))
                        .setQuantity(quantity)
                        .build();

                Order.OrderResponse response = orderServiceBlockingStub.createOrder(request);
                return "Order ID: " + response.getPid() ;
            }
        };
    }

    @Test
    void createOrder_ReturnsExpectedResponse() {
        String userId = "user123";
        String productId = "product123";
        int quantity = 1;
        String expectedResponse = "Order ID: order123, Supplier ID: supplier123";

        Order.OrderResponse grpcResponse = Order.OrderResponse.newBuilder()
                .setPid("order123")
                .build();

        Order.CreateOrderRequest grpcRequest = Order.CreateOrderRequest.newBuilder()
                .setUserPid(userId)
                .addAllProducts(List.of(Order.Product.newBuilder()
                .setPid(productId).build()))
                .setQuantity(quantity)
                .build();

        when(orderServiceBlockingStub.createOrder(grpcRequest)).thenReturn(grpcResponse);

        String actualResponse = orderService.createOrder();

        assertEquals(expectedResponse, actualResponse);
    }
}

