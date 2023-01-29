package com.hust.vitech.Controller;

import com.hust.vitech.Request.LoginRequest;
import com.hust.vitech.Request.UserRequest;
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

//    @PostMapping(value = "/newUser", produces = "application/json")
//    public ApiResponse<?> createNewUser(@Valid @RequestBody SignupRequest signupRequest) {
//        //Check if existed username or email
//        return userService.register(signupRequest);
//    }

    @PostMapping(value = "/register", produces = "application/json")
    public ApiResponse<?> register(@Valid @RequestBody UserRequest signupRequest) {
        return userService.register(signupRequest);
    }

    @GetMapping(value = "account", produces = "application/json")
    public ApiResponse<?> getCurrentUser() {
        return ApiResponse.successWithResult(userService.getCurrentUser());
    }

    @PutMapping(value = "account", produces =  "application/json")
    public ApiResponse<?> updateProfile(@RequestBody UserRequest userRequest){
        return ApiResponse.successWithResult(userService.updateProfile(userRequest));
    }

}
