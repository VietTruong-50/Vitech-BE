package com.hust.vitech.Controller;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Model.Order;
import com.hust.vitech.Model.Product;
import com.hust.vitech.Model.ShoppingSession;
import com.hust.vitech.Model.User;
import com.hust.vitech.Request.CartItemRequest;
import com.hust.vitech.Request.OrderRequest;
import com.hust.vitech.Request.UserRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.Impl.CartServiceImpl;
import com.hust.vitech.Service.Impl.OrderServiceImpl;
import com.hust.vitech.Service.Impl.UserServiceImpl;
import com.hust.vitech.Service.Impl.WishlistServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private OrderServiceImpl orderService;


    @GetMapping(value = "/users", produces = "application/json")
    public ApiResponse<Page<User>> getAllUsers(@RequestParam int size,
                                               @RequestParam int page,
                                               @RequestParam String sortBy) {
        return ApiResponse.successWithResult(userService.getAllUser(size, page, sortBy));
    }

    @GetMapping(value = "/account", produces = "application/json")
    public ApiResponse<User> getCurrentUser() {
        return ApiResponse.successWithResult(userService.getCurrentUser());
    }

    @PostMapping(value="/order/{orderId}")
    public ApiResponse<Order> changeOrderStatus(@PathVariable("orderId") Long orderId, @RequestParam("status") OrderStatusEnum orderStatusEnum){
        return ApiResponse.successWithResult(orderService.updateOrderStatus(orderId, orderStatusEnum));
    }

    @PutMapping(value = "/user/{userId}")
    public ApiResponse<User> updateUser(@PathVariable("userId") Long userId, @RequestBody UserRequest userRequest){
        return ApiResponse.successWithResult(userService.updateUser(userId, userRequest));
    }

    @GetMapping(value = "/user/{userId}")
    public ApiResponse<User> getUserById(@PathVariable("userId") Long userId){
        return ApiResponse.successWithResult(userService.getUserById(userId));
    }
}
