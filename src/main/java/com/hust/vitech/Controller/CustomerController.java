package com.hust.vitech.Controller;

import com.hust.vitech.Model.Order;
import com.hust.vitech.Model.Product;
import com.hust.vitech.Model.ShoppingSession;
import com.hust.vitech.Request.CartItemRequest;
import com.hust.vitech.Request.OrderRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.Impl.CartServiceImpl;
import com.hust.vitech.Service.Impl.OrderServiceImpl;
import com.hust.vitech.Service.Impl.WishlistServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/customer")
public class CustomerController {
    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private WishlistServiceImpl wishlistService;
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

    @PostMapping(value = "/checkout", produces = "application/json")
    public ApiResponse<Order> checkout(@RequestBody OrderRequest orderRequest){
        return ApiResponse.successWithResult(orderService.createOrder(orderRequest));
    }

    @PostMapping(value = "/wishlist/{id}")
    public ApiResponse<?> addToWishlist(@PathVariable("id") Long id) {
        wishlistService.addToWishlist(id);
        return ApiResponse.successWithResult("Add 1 product to wishlist");
    }

    @PutMapping(value = "/wishlist/{id}")
    public ApiResponse<?> removeFromWishlist(@PathVariable("id") Long id) {
        wishlistService.removeFromWishlist(id);
        return ApiResponse.successWithResult("Remove 1 product to wishlist");
    }

    @GetMapping(value = "/wishlist")
    public ApiResponse<Set<Product>> getUserWishlist() {
        return ApiResponse.successWithResult(wishlistService.getWishlist());
    }
}
