package com.hust.vitech.Controller;

import com.hust.vitech.Jwt.JwtUtils;
import com.hust.vitech.Model.Role;
import com.hust.vitech.Model.User;
import com.hust.vitech.Repository.RoleRepository;
import com.hust.vitech.Repository.UserRepository;
import com.hust.vitech.Request.LoginRequest;
import com.hust.vitech.Request.SignupRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Response.JwtResponse;
import com.hust.vitech.Response.MessageResponse;
import com.hust.vitech.Service.Impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping(value = "/signin", produces = "application/json")
    public ApiResponse<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Get token
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return ApiResponse.successWithResult(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping(value = "/signup", produces = "application/json")
    public ApiResponse<User> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

        //Check if existed username or email
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
        userRepository.save(user);

        return ApiResponse.successWithResult(user);
    }
}
