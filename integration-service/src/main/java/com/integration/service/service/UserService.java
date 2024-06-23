package com.integration.service.service;

import com.integration.User;

public interface UserService {

    User.UserResponse createUser(User.CreateUserRequest userRequest);
}
