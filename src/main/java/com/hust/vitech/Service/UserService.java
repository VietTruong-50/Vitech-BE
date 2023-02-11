package com.hust.vitech.Service;

import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.User;
import com.hust.vitech.Request.LoginRequest;
import com.hust.vitech.Request.UserRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Response.JwtResponse;
import com.hust.vitech.Response.StatisticQuantityResponse;
import com.hust.vitech.Response.StatisticValueResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    Page<User> getAllUser(int size, int page, String sortBy);

    JwtResponse login(LoginRequest loginRequest);

    ApiResponse<?> register(UserRequest signupRequest);

    ApiResponse<?> getCurrentUser();

    User updateUser(Long userId, UserRequest userRequest);

    ApiResponse<?> updateProfile(UserRequest userRequest);

    User getUserById(Long userId);

    void deleteUser(Long userId);

//    Page<User> filterUserByRole(List<String> listRoles, int page, int size, String sortBy);

    Page<User> filterUserByRole(String role, int page, int size, String sortBy);

    Page<Customer> findAllCustomer(int page, int size, String sortBy);

    StatisticQuantityResponse getStatistic();

    StatisticValueResponse getValuesByMonth();
}
