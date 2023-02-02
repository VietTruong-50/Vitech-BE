package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.Notification;
import com.hust.vitech.Repository.CustomerRepository;
import com.hust.vitech.Repository.NotificationRepository;
import com.hust.vitech.Request.UserRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Notification> getAllNotificationsByCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Customer customer = customerRepository.findCustomerByUserName(authentication.getName()).get();

        return notificationRepository.findAllByCustomer(customer);
    }
}
