package com.hust.vitech.Service.Impl;

import com.hust.vitech.Jwt.JwtUtils;
import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.Role;
import com.hust.vitech.Model.User;
import com.hust.vitech.Repository.CustomerRepository;
import com.hust.vitech.Repository.RoleRepository;
import com.hust.vitech.Repository.ShoppingSessionRepository;
import com.hust.vitech.Repository.UserRepository;
import com.hust.vitech.Request.LoginRequest;
import com.hust.vitech.Request.SignupRequest;
import com.hust.vitech.Request.UserRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Response.JwtResponse;
import com.hust.vitech.Response.MessageResponse;
import com.hust.vitech.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ShoppingSessionRepository shoppingSessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public Page<User> getAllUser(int size, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        //Get token
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        Optional<Customer> customer = customerRepository.findCustomerByUserName(userDetails.getUsername());

        if (customer.isPresent() && Objects.equals(roles.get(0), "ROLE_CUSTOMER")) {
            customer.get().setShoppingSession(
                    shoppingSessionRepository.findById(loginRequest.getShoppingSessionId()).orElse(null)
            );

            customerRepository.save(customer.get());
        }

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);
    }

    @Override
    public ApiResponse<?> register(SignupRequest signupRequest) {
        if (!signupRequest.isCustomer()) {
            if (userRepository.existsByUserName(signupRequest.getUsername())) {
                return ApiResponse.failureWithCode("", new MessageResponse("Error: Username is already taken!").getMessage());
            }

            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                return ApiResponse.failureWithCode("", new MessageResponse("Error: Email is already taken!").getMessage());
            }

            User user = new User(
                    signupRequest.getUsername(),
                    passwordEncoder.encode(signupRequest.getPassword()),
                    signupRequest.getEmail());

            //Get role from request
            Set<String> strRoles = signupRequest.getRoles();
            Set<Role> roles = new HashSet<>();

            //Add roles to user
            if (strRoles == null) {
                Role userRole = roleRepository.findByName("ROLE_CUSTOMER")
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(
                        role -> {
                            switch (role) {
                                case "admin" -> {
                                    Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                    roles.add(adminRole);
                                }
                                default -> {
                                    Role modRole = roleRepository.findByName("ROLE_MODERATOR")
                                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                    roles.add(modRole);
                                }
                            }
                        }
                );
            }

            user.setRoles(roles);

            return ApiResponse.successWithResult(userRepository.save(user));
        } else {
            if (customerRepository.existsByUserName(signupRequest.getUsername())) {
                return ApiResponse.failureWithCode("", new MessageResponse("Error: Username is already taken!").getMessage());
            }

            if (customerRepository.existsByEmail(signupRequest.getEmail())) {
                return ApiResponse.failureWithCode("", new MessageResponse("Error: Email is already taken!").getMessage());
            }

            Customer customer = new Customer(signupRequest.getUsername(),
                    passwordEncoder.encode(signupRequest.getPassword()),
                    signupRequest.getEmail());

            customer.setRole("ROLE_CUSTOMER");

            return ApiResponse.successWithResult(customerRepository.save(customer));

        }
    }


    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findUserByUserName(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User updateUser(Long userId, UserRequest userRequest) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            user.get().setUserName(userRequest.getUserName());
            user.get().setPassword(userRequest.getPassword());
            user.get().setEmail(userRequest.getEmail());
            user.get().setAddress(userRequest.getAddress());
            user.get().setSalary(userRequest.getSalary());

            Set<Role> roles = new HashSet<>();

            userRequest.getRoles().forEach(role -> {

                if (role.equals("admin")) {
                    Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role modRole = roleRepository.findByName("ROLE_MODERATOR")
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);
                }
            });

            user.get().setRoles(roles);

            return userRepository.save(user.get());
        }
        return null;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
