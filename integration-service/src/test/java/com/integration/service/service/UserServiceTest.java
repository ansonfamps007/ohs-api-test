package com.integration.service.service;

import com.google.protobuf.StringValue;
import com.integration.User;
import com.integration.UserServiceGrpc;
import com.integration.service.service.impl.UserServiceImpl;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Mock
    private UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    @InjectMocks
    private UserServiceImpl userService;

    private User.CreateUserRequest createUserRequest;
    private User.UserResponse userResponse;

    @BeforeEach
    void setUp() {
        createUserRequest = User.CreateUserRequest.newBuilder()
                .setFullName(StringValue.newBuilder()
                        .setValue("John Doe").build())
                .setEmail("john.doe@example.com")
                .build();

        userResponse = User.UserResponse.newBuilder()
                .setPid("12345")
                .build();
    }

    @Test
    void createUser_ReturnsUserResponse_WhenSuccessful() {
        when(userServiceStub.createUser(any(User.CreateUserRequest.class))).thenReturn(userResponse);

        User.UserResponse response = userService.createUser(createUserRequest);

        assertThat(response.getPid()).isEqualTo("12345");
    }

    @Test
    void createUser_ThrowsRuntimeException_WhenGrpcFails() {
        doThrow(new StatusRuntimeException(io.grpc.Status.INTERNAL)).when(userServiceStub).createUser(any(User.CreateUserRequest.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(createUserRequest);
        });

        verify(userServiceStub, Mockito.times(1)).createUser(any(User.CreateUserRequest.class));
    }

}
