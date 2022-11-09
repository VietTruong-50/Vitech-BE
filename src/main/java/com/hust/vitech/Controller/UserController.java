package com.hust.vitech.Controller;

import com.hust.vitech.Model.User;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping(value = "/users", produces = "application/json")
    public ApiResponse<Page<User>> getAllUsers(@RequestParam int size,
                                               @RequestParam int page,
                                               @RequestParam String sortBy){
        return ApiResponse.successWithResult(userService.getAllUser(size, page, sortBy));
    }
}
