package com.hust.vitech.Service;

import com.hust.vitech.Model.ShoppingSession;
import com.hust.vitech.Model.User;
import com.hust.vitech.Request.CartItemRequest;
import com.hust.vitech.Request.LoginRequest;
import com.hust.vitech.Request.SignupRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Response.JwtResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<User> getAllUser(int size, int page, String sortBy);

    JwtResponse login(LoginRequest loginRequest);
    ApiResponse<User> register(SignupRequest signupRequest);
    User getUserById(Long id);
}
