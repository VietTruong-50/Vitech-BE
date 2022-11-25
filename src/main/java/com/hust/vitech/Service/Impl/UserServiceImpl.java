package com.hust.vitech.Service.Impl;

import com.hust.vitech.Jwt.JwtUtils;
import com.hust.vitech.Model.CartItem;
import com.hust.vitech.Model.Role;
import com.hust.vitech.Model.ShoppingSession;
import com.hust.vitech.Model.User;
import com.hust.vitech.Repository.CartItemRepository;
import com.hust.vitech.Repository.RoleRepository;
import com.hust.vitech.Repository.ShoppingSessionRepository;
import com.hust.vitech.Repository.UserRepository;
import com.hust.vitech.Request.CartItemRequest;
import com.hust.vitech.Request.LoginRequest;
import com.hust.vitech.Request.SignupRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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

//        SecurityContextHolder.getContext().setAuthentication(authentication);

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
    public ApiResponse<User> register(SignupRequest signupRequest) {
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
            Role userRole = roleRepository.findByName("ROLE_USER")
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
                            case "mod" -> {
                                Role modRole = roleRepository.findByName("ROLE_MODERATOR")
                                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                roles.add(modRole);
                            }
                            default -> {
                                Role userRole = roleRepository.findByName("ROLE_USER")
                                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                roles.add(userRole);
                            }
                        }
                    }
            );
        }

        user.setRoles(roles);
        user.setShoppingSession(new ShoppingSession());

        return ApiResponse.successWithResult(userRepository.save(user));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
