package com.hust.vitech.Controller;

import com.hust.vitech.Model.ShoppingSession;
import com.hust.vitech.Model.User;
import com.hust.vitech.Request.CartItemRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.Impl.CartServiceImpl;
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

    @Autowired
    private CartServiceImpl cartService;

    @GetMapping(value = "/users", produces = "application/json")
    public ApiResponse<Page<User>> getAllUsers(@RequestParam int size,
                                               @RequestParam int page,
                                               @RequestParam String sortBy){
        return ApiResponse.successWithResult(userService.getAllUser(size, page, sortBy));
    }

    @GetMapping(value = "/account/{id}", produces = "application/json")
    public ApiResponse<User> getUserById(@PathVariable("id") Long id){
        return ApiResponse.successWithResult(userService.getUserById(id));
    }

    @GetMapping(value = "/cart/{id}", produces = "application/json")
    public ApiResponse<ShoppingSession> getShoppingCart(@PathVariable("id") Long id){
        return ApiResponse.successWithResult(cartService.getShoppingCart(id));
    }

    @GetMapping(value = "/cart/{id}/total", produces = "application/json")
    public ApiResponse<Long> getTotalValues(@PathVariable("id") Long id){
        return ApiResponse.successWithResult(cartService.getTotalValues(id));
    }

    @PostMapping(value = "/cart", produces = "application/json")
    public ApiResponse<?> addItemToCart(@RequestBody CartItemRequest cartItemRequest) {
        try {
            cartService.addItemToCart(cartItemRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.successWithResult("Add 1 item to shopping cart");
    }

    @DeleteMapping(value = "/cart/{id}/product/{productId}", produces = "application/json")
    public ApiResponse<?> removeItemFromCart(@PathVariable("id") Long shoppingSessionId,
                                             @PathVariable("productId") Long productId){
        cartService.removeItemFromCart(shoppingSessionId, productId);
        return ApiResponse.successWithResult("Remove 1 item from shopping cart");
    }

}
