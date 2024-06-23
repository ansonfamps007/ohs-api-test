package com.integration.service.controller;

import com.integration.service.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/create")
    public ResponseEntity<String> createOrder() {
        log.info("OrderController - createOrder");
        return ResponseEntity.ok(orderService.createOrder());
    }
}
