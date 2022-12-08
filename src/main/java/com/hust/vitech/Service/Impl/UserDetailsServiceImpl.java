package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.User;
import com.hust.vitech.Repository.CustomerRepository;
import com.hust.vitech.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username)
                .orElse(null);

        if (user != null) {
            return UserDetailsImpl.build(user, null);
        }

        Customer customer = customerRepository.findCustomerByUserName(username).orElse(null);

        if (customer != null) {
            return UserDetailsImpl.build(null, customer);
        }

        return null;
    }

}
