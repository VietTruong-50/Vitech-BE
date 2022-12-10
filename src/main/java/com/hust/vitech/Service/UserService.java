package com.hust.vitech.Service;

import com.hust.vitech.Model.User;
import com.hust.vitech.Request.LoginRequest;
import com.hust.vitech.Request.SignupRequest;
import com.hust.vitech.Request.UserRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Response.JwtResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<User> getAllUser(int size, int page, String sortBy);

    JwtResponse login(LoginRequest loginRequest);

    ApiResponse<?> register(SignupRequest signupRequest);

    User getCurrentUser();

    User updateUser(Long userId, UserRequest userRequest);

    User getUserById(Long userId);
}
