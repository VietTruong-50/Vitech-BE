package com.hust.vitech.Controller;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.Order;
import com.hust.vitech.Model.User;
import com.hust.vitech.Request.OrderRequest;
import com.hust.vitech.Request.UserRequest;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Response.StatisticQuantityResponse;
import com.hust.vitech.Response.StatisticValueResponse;
import com.hust.vitech.Service.Impl.OrderServiceImpl;
import com.hust.vitech.Service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping(value = "/order/{orderId}", produces = "application/json")
    public ApiResponse<Order> changeOrderStatus(@PathVariable("orderId") Long orderId,
                                                @RequestParam("status") OrderStatusEnum orderStatusEnum) {
        return ApiResponse.successWithResult(orderService.updateOrderStatus(orderId, orderStatusEnum));
    }

    @PutMapping(value = "/orders/{orderId}", produces = "application/json")
    public ApiResponse<Order> updateOrder(@PathVariable("orderId") Long orderId,
                                          @RequestBody() OrderRequest orderRequest) {
        return ApiResponse.successWithResult(orderService.updateOrder(orderId, orderRequest));
    }

    @GetMapping(value = "/admin/orders", produces = "application/json")
    public ApiResponse<Page<Order>> getAllOrders(@RequestParam int page,
                                                 @RequestParam int size,
                                                 @RequestParam String sortBy) {
        return ApiResponse.successWithResult(orderService.getAllOrders(page, size, sortBy));
    }

    @PutMapping(value = "/user/{userId}", produces = "application/json")
    public ApiResponse<User> updateUser(@PathVariable("userId") Long userId, @RequestBody UserRequest userRequest) {
        return ApiResponse.successWithResult(userService.updateUser(userId, userRequest));
    }

    @DeleteMapping(value = "/user/{userId}", produces = "application/json")
    public ApiResponse<?> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.successWithResult("Remove user success");
    }

    @GetMapping(value = "/user/{userId}", produces = "application/json")
    public ApiResponse<User> getUserById(@PathVariable("userId") Long userId) {
        return ApiResponse.successWithResult(userService.getUserById(userId));
    }

    @GetMapping(value = "/users/{role}", produces = "application/json")
    public ApiResponse<Page<User>> findAllByRole(@PathVariable("role") String role,
                                                 @RequestParam int page,
                                                 @RequestParam int size,
                                                 @RequestParam String sortBy) {
        return ApiResponse.successWithResult(userService.filterUserByRole(role, page, size, sortBy));
    }

    @GetMapping(value = "/customers", produces = "application/json")
    public ApiResponse<Page<Customer>> findAllCustomers(@RequestParam int page,
                                                        @RequestParam int size,
                                                        @RequestParam String sortBy) {
        return ApiResponse.successWithResult(userService.findAllCustomer(page, size, sortBy));
    }

    @GetMapping(value = "/quantity", produces = "application/json")
    public ApiResponse<StatisticQuantityResponse> getStatistic() {
        return ApiResponse.successWithResult(userService.getStatistic());
    }

    @GetMapping(value = "/statistic/month", produces = "application/json")
    public ApiResponse<StatisticValueResponse> getValuesByMonth() {
        return ApiResponse.successWithResult(userService.getValuesByMonth());
    }
}
