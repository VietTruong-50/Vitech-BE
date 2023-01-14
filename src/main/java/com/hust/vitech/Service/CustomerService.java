package com.hust.vitech.Service;

import com.hust.vitech.Model.Comment;
import com.hust.vitech.Model.Customer;
import com.hust.vitech.Request.CustomerRequest;
import com.hust.vitech.Request.LoginRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Response.JwtResponse;

public interface CustomerService {
    JwtResponse login(LoginRequest loginRequest);

    ApiResponse<Customer> register(CustomerRequest customerRequest);

    Customer getCurrentUser();

}

