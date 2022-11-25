package com.hust.vitech.Controller;

import com.hust.vitech.Model.User;
import com.hust.vitech.Request.LoginRequest;
import com.hust.vitech.Request.SignupRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Response.JwtResponse;
import com.hust.vitech.Service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/signin", produces = "application/json")
    public ApiResponse<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ApiResponse.successWithResult(userService.login(loginRequest));
    }

    @PostMapping(value = "/signup", produces = "application/json")
    public ApiResponse<User> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        //Check if existed username or email
        return userService.register(signupRequest);
    }
}
