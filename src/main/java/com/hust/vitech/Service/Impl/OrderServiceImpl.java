package com.hust.vitech.Service.Impl;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Model.*;
import com.hust.vitech.Repository.*;
import com.hust.vitech.Request.OrderRequest;
import com.hust.vitech.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
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
    private CustomerRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Order createOrder(OrderRequest orderRequest) {
        Order order = new Order();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        Customer customer = userRepository
                .findCustomerByUserName(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        order.setOrderCode(randomString(12));
        order.setShippingMethod(shippingMethodRepository
                .findById(orderRequest.getShippingMethodId()).orElse(null));
        order.setCustomer(customer);
        order.setStatus(orderRequest.getOrderStatusEnum());
        order.setOrderDate(LocalDate.now());

        Set<CartItem> cartItems = cartItemRepository
                .findAllByShoppingSessionId(customer.getShoppingSession().getId());

        if (!cartItems.isEmpty()) {
            cartItems.forEach(item -> {
                        Product product = item.getProduct();

                        product.setQuantity(product.getQuantity() - item.getQuantity());

                        orderRepository.save(order);

                        orderDetailRepository
                                .save(new OrderDetail(order,
                                        product,
                                        item.getQuantity(),
                                        item.getProduct().getActualPrice()));

                        productRepository.save(product);
                    }
            );
        }
        order.setTotal(shoppingSessionRepository.getTotalValues(customer.getShoppingSession().getId()));

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

    @Override
    public List<Order> getCurrentOrders() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        Customer customer = userRepository
                .findCustomerByUserName(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return orderRepository.findAllByCustomer(customer);
    }

    @Override
    public Order getOrderByCode(String code) {
        return orderRepository.findByOrderCode(code);
    }

    //Generate order code

    static final String AB = "0123456789";
    static SecureRandom rnd = new SecureRandom();

    private String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
