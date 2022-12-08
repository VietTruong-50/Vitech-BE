package com.hust.vitech.Service.Impl;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Model.CartItem;
import com.hust.vitech.Model.Order;
import com.hust.vitech.Model.OrderDetail;
import com.hust.vitech.Model.User;
import com.hust.vitech.Repository.*;
import com.hust.vitech.Request.OrderRequest;
import com.hust.vitech.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ShoppingSessionRepository shoppingSessionRepository;

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Order createOrder(OrderRequest orderRequest) {
        Order order = new Order();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        User user = userRepository.findUserByUserName(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        order.setOrderCode(orderRequest.getOrderCode());
        order.setShippingMethod(shippingMethodRepository
                .findById(orderRequest.getShippingMethodId()).orElse(null));
        order.setUser(user);
        order.setStatus(orderRequest.getOrderStatusEnum());

        Set<CartItem> cartItems = cartItemRepository
                .findAllByShoppingSessionId(user.getShoppingSession().getId());

        if (!cartItems.isEmpty()) {
            cartItems.forEach(item ->
                    orderDetailRepository
                            .save(new OrderDetail(order,
                                    item.getProduct(),
                                    item.getQuantity(),
                                    item.getProduct().getActualPrice())));
        }
        order.setTotal(shoppingSessionRepository.getTotalValues(user.getShoppingSession().getId()));

        return orderRepository.save(order);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatusEnum statusEnum) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(statusEnum);

        return orderRepository.save(order);
    }

    @Override
    public void destroyOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        orderDetailRepository.deleteAllByOrder(order);

        orderRepository.delete(order);
    }


}
