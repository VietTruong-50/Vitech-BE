package com.hust.vitech.Service.Impl;

import com.hust.vitech.Jwt.JwtUtils;
import com.hust.vitech.Model.*;
import com.hust.vitech.Repository.*;
import com.hust.vitech.Request.LoginRequest;
import com.hust.vitech.Request.UserRequest;
import com.hust.vitech.Response.*;
import com.hust.vitech.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
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
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartServiceImpl cartService;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);
    }

    @Override
    public ApiResponse<?> register(UserRequest signupRequest) {
        if (!signupRequest.isCustomer()) {
            if (userRepository.existsByUserName(signupRequest.getUserName())) {

                return ApiResponse.failureWithCode("", new MessageResponse("Error: Username is already taken!").getMessage());
            }

            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                return ApiResponse.failureWithCode("", new MessageResponse("Error: Email is already taken!").getMessage());
            }

            User user = new User(
                    signupRequest.getUserName(),
                    passwordEncoder.encode(signupRequest.getPassword()),
                    signupRequest.getEmail());

            //Get role from request
            List<String> strRoles = signupRequest.getRoles();
            Set<Role> roles = new HashSet<>();

            //Add roles to user
            if (strRoles == null) {
                Role userRole = roleRepository.findByName("ROLE_CUSTOMER")
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(
                        role -> {
                            if (role.equals("admin")) {
                                Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                roles.add(adminRole);
                            } else {
                                Role modRole = roleRepository.findByName("ROLE_MODERATOR")
                                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                roles.add(modRole);
                            }
                        }
                );
            }

            user.setRoles(roles);

            return ApiResponse.successWithResult(userRepository.save(user));
        } else {
            if (customerRepository.existsByUserName(signupRequest.getUserName())) {
                return ApiResponse.failureWithCode("", new MessageResponse("Error: Username is already taken!").getMessage());
            }

            if (customerRepository.existsByEmail(signupRequest.getEmail())) {
                return ApiResponse.failureWithCode("", new MessageResponse("Error: Email is already taken!").getMessage());
            }

            Customer customer = new Customer(signupRequest.getUserName(),
                    passwordEncoder.encode(signupRequest.getPassword()),
                    signupRequest.getEmail(), signupRequest.getGenderEnum(), "ROLE_CUSTOMER",
                    signupRequest.getFullName(), signupRequest.getPhone(), signupRequest.getDateOfBirth(),
                    cartService.createShoppingSession());

            return ApiResponse.successWithResult(customerRepository.save(customer));

        }
    }


    @Override
    public ApiResponse<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (userRepository.existsByUserName(authentication.getName())) {
            return ApiResponse.successWithResult(userRepository.findUserByUserName(authentication.getName()).get());
        } else if (customerRepository.existsByUserName(authentication.getName())) {
            return ApiResponse.successWithResult(customerRepository.findCustomerByUserName(authentication.getName()).get());
        }
        return null;
    }

    @Override
    public User updateUser(Long userId, UserRequest userRequest) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            user.get().setUserName(userRequest.getUserName());
            user.get().setPassword(userRequest.getPassword());
            user.get().setEmail(userRequest.getEmail());

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
    public ApiResponse<?> updateProfile(UserRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = null;
        Customer customer = null;

        if (userRepository.existsByUserName(authentication.getName())) {
            user = userRepository.findUserByUserName(authentication.getName()).get();
        } else if (customerRepository.existsByUserName(authentication.getName())) {
            customer = customerRepository.findCustomerByUserName(authentication.getName()).get();
        }

        if (customer != null) {
            customer.setEmail(userRequest.getEmail());
            customer.setFullName(userRequest.getFullName());
            customer.setGenderEnum(userRequest.getGenderEnum());
            customer.setFullName(userRequest.getFullName());
            customer.setDateOfBirth(userRequest.getDateOfBirth());

            return ApiResponse.successWithResult(customerRepository.save(customer));
        } else if (user != null) {
            user.setFullName(userRequest.getFullName());
            user.setGenderEnum(userRequest.getGenderEnum());
            user.setFullName(userRequest.getFullName());
            user.setDateOfBirth(userRequest.getDateOfBirth());
            user.setEmail(userRequest.getEmail());

            return ApiResponse.successWithResult(userRepository.save(user));
        }

        return null;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                }
        ).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public Page<User> filterUserByRole(String role, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return userRepository.findAllByRole(role, pageable);
    }

    @Override
    public Page<Customer> findAllCustomer(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return customerRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> searchAllCustomer(String searchText, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return customerRepository.findAllByUserNameOrPhoneOrEmailContains(searchText, pageable);
    }

    @Override
    public StatisticQuantityResponse getStatistic() {
        List<Customer> customers = customerRepository.findAll();
        List<Order> orders = orderRepository.findAll();
        List<Product> products = productRepository.findAll();
        return new StatisticQuantityResponse(products.size(), customers.size(), orders.size());
    }

    @Override
    public StatisticValueResponse getValuesByMonth() {
        StatisticValueResponse statisticValueResponse = new StatisticValueResponse();

        List<Double> saleStatistic = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            saleStatistic.add(userRepository.getTotalValueByMonth(i));
        }

        List<Integer> orderStatistic = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            orderStatistic.add(userRepository.getOrderByStatusInYear(i));
        }

        statisticValueResponse.setSaleStatistic(saleStatistic);
        statisticValueResponse.setOrderStatistic(orderStatistic);

        return statisticValueResponse;
    }


//    @Override
//    public Page<User> filterUserByRole(List<String> listRoles, int page, int size, String sortBy) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
//
//        List<User> users = new ArrayList<>();
//
//        for (String role : listRoles) {
//            if (role.equals("admin")) {
//                users.addAll(userRepository.findAllByRole("ROLE_ADMIN"));
//            } else if (role.equals("mod")) {
//                users = userRepository.findAllByRole("ROLE_MODERATOR");
//            }
//        };
//
//        return new PageImpl<>(users, pageable, users.size());
//    }
}
