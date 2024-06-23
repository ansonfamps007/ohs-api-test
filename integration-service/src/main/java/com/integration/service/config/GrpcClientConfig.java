package com.integration.service.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel userServiceChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 50054)
                .usePlaintext()
                .build();
    }

    @Bean
    public ManagedChannel orderServiceChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 50053)
                .usePlaintext()
                .build();
    }

    @Bean
    public ManagedChannel productServiceChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();
    }

    @Bean
    public ManagedChannel supplierServiceChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
    }
}
