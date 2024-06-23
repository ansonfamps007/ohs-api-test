package com.integration.service.service.impl;

import com.integration.User;
import com.integration.UserServiceGrpc;
import com.integration.service.service.UserService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String USER_CREATE_FAILURE_MSG = "Order has been created";

    @Value("${app.file.path}")
    private String filePath;

    @Value("${user.address}")
    private String userAddress;

    @Value("${user.port}")
    private String userPort;

    private final UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    public UserServiceImpl() {
        ManagedChannel userChannel = ManagedChannelBuilder.forAddress(
                        "localhost", 50054)
                .usePlaintext()
                .build();
        this.userServiceStub = UserServiceGrpc.newBlockingStub(userChannel);
    }

    /*@GrpcClient("userServiceChannel")
    private ManagedChannel userChannel;*/


    @Override
    public User.UserResponse createUser(User.CreateUserRequest userRequest) {

        try {
            return userServiceStub.createUser(userRequest);
        } catch (Exception ex) {
            log.error(USER_CREATE_FAILURE_MSG + "createUser {} - {}", userRequest, ex.getMessage());
            throw new RuntimeException();
        }
    }
}
