package com.hust.vitech.Controller;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Model.*;
import com.hust.vitech.Repository.AddressRepository;
import com.hust.vitech.Request.*;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Response.CustomCustomerResponse;
import com.hust.vitech.Service.Impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerServiceImpl customerService;


    @GetMapping(value = "/cart", produces = "application/json")
    public ApiResponse<ShoppingSession> getShoppingCart() {
        return ApiResponse.successWithResult(cartService.getShoppingCart());
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
            return ApiResponse.failureWithCode("404", e.getMessage());
        }
    }

    @PutMapping(value = "/cart", produces = "application/json")
    public ApiResponse< List<CartItem>> updateCurrentCart(@RequestParam(required = false) List<CartItem> cartItems) {
        try {
            return ApiResponse.successWithResult(cartService.updateCurrentCart(cartItems));
        } catch (Exception e) {
            return ApiResponse.failureWithCode("404", e.getMessage());
        }
    }

    @DeleteMapping(value = "/cart/product/{productId}", produces = "application/json")
    public ApiResponse<?> removeItemFromCart(@PathVariable("productId") Long productId) {
        cartService.removeItemFromCart(productId);
        return ApiResponse.successWithResult("Remove 1 item from shopping cart");
    }

    @PostMapping(value = "/checkout", produces = "application/json")
    public ApiResponse<Order> checkout(@RequestBody OrderRequest orderRequest) {
        return ApiResponse.successWithResult(orderService.createOrder(orderRequest));
    }

    @GetMapping(value = "/orders", produces = "application/json")
    public ApiResponse<Page<Order>> getCurrentOrders(@RequestParam("status") OrderStatusEnum orderStatusEnum,
                                                     @RequestParam int page,
                                                     @RequestParam int size,
                                                     @RequestParam String sortBy) {
        return ApiResponse.successWithResult(orderService.getCurrentOrdersByStatus(orderStatusEnum, page, size, sortBy));
    }

    @GetMapping(value = "/orders/{orderCode}", produces = "application/json")
    public ApiResponse<Order> getOrderByCode(@PathVariable("orderCode") String orderCode) {
        return ApiResponse.successWithResult(orderService.getOrderByCode(orderCode));
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
    public ApiResponse<List<Product>> getUserWishlist() {
        return ApiResponse.successWithResult(wishlistService.getWishlist());
    }

    @PostMapping(value = "/comment")
    public ApiResponse<Comment> createComment(@RequestBody CommentRequest commentRequest) {
        return ApiResponse.successWithResult(commentService.createComment(commentRequest));
    }

    @DeleteMapping(value = "/comment/{commentId}")
    public ApiResponse<Comment> deleteComment(@PathVariable("commentId") Long commentId) {
        return ApiResponse.successWithResult(commentService.deleteComment(commentId));
    }

    @GetMapping(value = "/product/{id}/comment", produces = "application/json")
    public ApiResponse<Page<Comment>> getCommentPagination(@PathVariable("id") Long productId,
                                                           @RequestParam int page,
                                                           @RequestParam int size,
                                                           @RequestParam String sortBy,
                                                           @RequestParam String orderBy) {
        return ApiResponse.successWithResult(commentService.getCommentPagination(productId, page, size, sortBy, orderBy));
    }

    @GetMapping(value = "/products/filter", produces = "application/json")
    public ApiResponse<Page<Product>> filterProduct(@RequestParam int page,
                                                    @RequestParam int size,
                                                    @RequestParam String sortBy,
                                                    @RequestParam(required = false) List<String> categories,
                                                    @RequestParam(required = false) List<String> subCategories,
                                                    @RequestParam(required = false) int firstPrice,
                                                    @RequestParam(required = false) int secondPrice,
                                                    @RequestParam(required = false) String searchText
    ) {
        return ApiResponse.successWithResult(productService.filterProduct(categories, subCategories, firstPrice, secondPrice, page, size, sortBy, searchText));
    }

    @GetMapping(value = "/notifications", produces = "application/json")
    public ApiResponse<List<Notification>> getAllNotifications() {
        return ApiResponse.successWithResult(customerService.getAllNotificationsByCustomer());
    }

    @PostMapping(value = "/address", produces = "application/json")
    public ApiResponse<Address> createNewAddress(@RequestBody AddressRequest addressRequest) {
        return ApiResponse.successWithResult(customerService.createNewAddress(addressRequest));
    }

    @PutMapping(value = "/address/{addressId}", produces = "application/json")
    public ApiResponse<Address> editAddress(@PathVariable("addressId") Long addressId, @RequestBody AddressRequest addressRequest) {
        return ApiResponse.successWithResult(customerService.editAddress(addressId, addressRequest));
    }

    @DeleteMapping(value = "/address/{addressId}", produces = "application/json")
    public ApiResponse<Address> deleteAddress(@PathVariable("addressId") Long addressId) {
        return ApiResponse.successWithResult(customerService.deleteAddress(addressId));
    }

    @GetMapping(value = "/address/default", produces = "application/json")
    public ApiResponse<Address> getDefaultAddress() {
        return ApiResponse.successWithResult(customerService.getDefaultAddress());
    }

    @GetMapping(value = "/address", produces = "application/json")
    public ApiResponse<List<Address>> getAllAddress() {
        return ApiResponse.successWithResult(customerService.getListAddress());
    }

    @GetMapping(value = "/detail/{id}", produces = "application/json")
    public ApiResponse<CustomCustomerResponse> getCustomerById(@PathVariable("id") Long id) {
        return ApiResponse.successWithResult(customerService.getCustomerById(id));
    }

    @PutMapping(value = "/detail/{id}", produces = "application/json")
    public ApiResponse<Customer> editCustomer(@PathVariable("id") Long id, @RequestBody CustomerRequest customerRequest) {
        return ApiResponse.successWithResult(customerService.editCustomer(id, customerRequest));
    }

    @GetMapping(value = "/address/{addressId}", produces = "application/json")
    public ApiResponse<Address> getAddressById(@PathVariable("addressId") Long addressId) {
        return ApiResponse.successWithResult(addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address not found")));
    }
}
