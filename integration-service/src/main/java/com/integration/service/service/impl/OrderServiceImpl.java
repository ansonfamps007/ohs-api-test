package com.integration.service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.StringValue;
import com.integration.Order;
import com.integration.OrderServiceGrpc;
import com.integration.User;
import com.integration.service.dto.OrderDto;
import com.integration.service.dto.ProcessedOrder;
import com.integration.service.exception.ResourceNotFoundException;
import com.integration.service.service.OrderService;
import com.integration.service.service.UserService;
import com.opencsv.bean.CsvToBeanBuilder;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_CREATE_MSG = "Order has been created";

    private static final String NO_DATA = "Not data available";

    private final UserService userService;

    @Value("${order.address}")
    private String orderAddress;

    @Value("${order.port}")
    private String orderPort;

    //@GrpcClient("orderServiceChannel")
    //private ManagedChannel orderServiceChannel;
    @Value("${app.file.path}")
    private String filePath;

    @Value("${app.file.processed-order}")
    private String processedOrderFile;

    private final OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    public OrderServiceImpl(UserService userService) {
        this.userService = userService;
        ManagedChannel orderServiceChannel = ManagedChannelBuilder.forAddress(
                        "localhost", 50053)
                .usePlaintext()
                .build();
        orderServiceBlockingStub = OrderServiceGrpc
                .newBlockingStub(orderServiceChannel);
    }

    private static User.ShippingAddress mapShippingAddress(OrderDto order) {
        return User.ShippingAddress.newBuilder()
                .setAddress(StringValue.newBuilder()
                        .setValue(order.getShippingAddress()).build()).build();
    }

    private static User.PaymentMethod mapPaymentMethod(OrderDto order) {
        return User.PaymentMethod.newBuilder()
                .setCreditCardNumber(StringValue.newBuilder()
                        .setValue(order.getCreditCardNumber()).build())
                .setCreditCardType(StringValue.newBuilder()
                        .setValue(order.getCreditCardType()).build()).build();
    }

    private static User.CreateUserRequest mapUserRequest(OrderDto order, User.ShippingAddress shippingAddress, User.PaymentMethod paymentMethod) {
        return User.CreateUserRequest.newBuilder()
                .setFullName(StringValue.newBuilder()
                        .setValue(order.getFullName()).build())
                .setEmail(order.getEmail())
                .setAddress(shippingAddress)
                .addAllPaymentMethods(Collections.singletonList(paymentMethod))
                .build();
    }

    private static Order.CreateOrderRequest mapOrderRequest(OrderDto order) {
        return Order.CreateOrderRequest.newBuilder()
                .addAllProducts(List.of(Order.Product.newBuilder()
                        .setPid(order.getProductPid()).build())).build();
    }

    private static ProcessedOrder mapProccessedOrder(OrderDto order, String userPid, Order.OrderResponse orderResponse) {
        return ProcessedOrder.builder()
                .userPid(userPid)
                .orderPid(orderResponse.getPid())
                .supplierPid(order.getSupplierPid())
                .build();
    }

    @Override
    public String createOrder() {
        log.info("OrderService - createOrder");

        List<OrderDto> orderList = processOrders();
        if (null != orderList && orderList.size() > 0) {
            for (OrderDto order : orderList) {
                User.ShippingAddress shippingAddress = mapShippingAddress(order);
                User.PaymentMethod paymentMethod = mapPaymentMethod(order);
                User.CreateUserRequest userRequest = mapUserRequest(order, shippingAddress, paymentMethod);
                User.UserResponse userResponse = userService.createUser(userRequest);
                if (null != userResponse) {
                    String userPid = userResponse.getPid();
                    Order.CreateOrderRequest createOrderRequest = mapOrderRequest(order);
                    Order.OrderResponse orderResponse = orderServiceBlockingStub.createOrder(createOrderRequest);
                    if (null != orderResponse) {
                        ProcessedOrder processedOrder = mapProccessedOrder(order, userPid, orderResponse);
                        generateProccessedOrder(processedOrder);
                    }
                }
            }
            return ORDER_CREATE_MSG;
        } else {
            log.error("OrderService - createOrder, " + NO_DATA + " orderList {}", orderList);
            throw new ResourceNotFoundException("Failed to create order !, " + NO_DATA);
        }
    }

    private void generateProccessedOrder(ProcessedOrder processedOrder) {
        log.info("OrderService - generateProccessedOrder");
        File file = new File(processedOrderFile);
        if (!file.exists()) {
            try {
                Files.createFile(Paths.get(processedOrderFile));
            } catch (IOException ex) {
                log.error("OrderService - generateProccessedOrder, unable to read the file ! {} - {}", processedOrderFile, ex.getMessage());
                throw new ResourceNotFoundException(ex.getMessage());
            }
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writeValue(file, processedOrder);
            } catch (IOException ex) {
                log.error("OrderService - generateProccessedOrder, unable to write into the file ! {} - {}", processedOrderFile, ex.getMessage());
                throw new ResourceNotFoundException(ex.getMessage());
            }
        }
    }

    private List processOrders() {
        try {
            return new CsvToBeanBuilder(new FileReader(filePath))
                    .withType(OrderDto.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException ex) {
            log.error("OrderService - processOrders, file not found ! {} - {}", filePath, ex.getMessage());
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }
}
