package com.hust.vitech.Service;

import com.hust.vitech.Model.Comment;
import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.Notification;
import com.hust.vitech.Request.CustomerRequest;
import com.hust.vitech.Request.LoginRequest;
import com.hust.vitech.Request.UserRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Response.JwtResponse;

import java.util.List;

public interface CustomerService {
    List<Notification> getAllNotificationsByCustomer();
}

