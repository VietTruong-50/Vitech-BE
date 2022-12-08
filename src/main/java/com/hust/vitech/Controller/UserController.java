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
                                               @RequestParam String sortBy) {
        return ApiResponse.successWithResult(userService.getAllUser(size, page, sortBy));
    }

    @GetMapping(value = "/account", produces = "application/json")
    public ApiResponse<User> getCurrentUser() {
        return ApiResponse.successWithResult(userService.getCurrentUser());
    }

    @GetMapping(value = "/cart/{id}", produces = "application/json")
    public ApiResponse<ShoppingSession> getShoppingCart(@PathVariable("id") Long id) {
        return ApiResponse.successWithResult(cartService.getShoppingCart(id));
    }

    @GetMapping(value = "/cart/{id}/total", produces = "application/json")
    public ApiResponse<Double> getTotalValues(@PathVariable("id") Long id) {
        return ApiResponse.successWithResult(cartService.getTotalValues(id));
    }

    @PostMapping(value = "/cart", produces = "application/json")
    public ApiResponse<ShoppingSession> addItemToCart(@RequestBody CartItemRequest cartItemRequest) {
        try {
            return ApiResponse.
                    successWithResult(cartService.addItemToCart(cartItemRequest),
                            "Add 1 item to shopping cart");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping(value = "/cart/{id}/product/{productId}", produces = "application/json")
    public ApiResponse<?> removeItemFromCart(@PathVariable("id") Long shoppingSessionId,
                                             @PathVariable("productId") Long productId) {
        cartService.removeItemFromCart(shoppingSessionId, productId);
        return ApiResponse.successWithResult("Remove 1 item from shopping cart");
    }

}
