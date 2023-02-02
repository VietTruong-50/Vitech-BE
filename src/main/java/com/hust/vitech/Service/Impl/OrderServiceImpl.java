package com.hust.vitech.Service.Impl;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Enum.PaymentMethodEnum;
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
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CardPaymentRepository cardPaymentRepository;

    @Override
    public Order createOrder(OrderRequest orderRequest) {

        CardPayment cardPayment = null;

        Order order = new Order();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        Customer customer = customerRepository
                .findCustomerByUserName(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (orderRequest.getPaymentMethodEnum() == PaymentMethodEnum.ONLINE_PAYING) {
            cardPayment = cardPaymentRepository.findByCardNumber(orderRequest.getCardNumber())
                    .orElse(null);

            if (cardPayment == null) {
                cardPayment = new CardPayment();

                cardPayment.setCardNumber(orderRequest.getCardNumber());
                cardPayment.setCustomer(customer);

                cardPaymentRepository.save(cardPayment);
            }
        }

        order.setOrderCode(randomString());
        order.setShippingMethod(shippingMethodRepository
                .findById(orderRequest.getShippingMethodId()).orElse(null));
        order.setCustomer(customer);
        order.setStatus(OrderStatusEnum.WAITING_PROCESS);
        order.setOrderDate(LocalDate.now());
        order.setCardPayment(cardPayment);
        order.setPaymentMethodEnum(orderRequest.getPaymentMethodEnum());

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

        Notification notification = new Notification();

        notification.setOrder(order);
        notification.setCustomer(customer);
        notification.setMessage("Đặt hàng thành công");

        notificationRepository.save(notification);

        return orderRepository.save(order);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatusEnum statusEnum) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(statusEnum);

        Notification notification = new Notification();

        Customer customer = customerRepository.findById(order.getCustomer()
                .getId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (statusEnum.equals(OrderStatusEnum.WAITING_DELIVERY)) {
            order.setDeliveryDate(LocalDate.now().plusDays(2));
        } else if (statusEnum.equals(OrderStatusEnum.SUCCESS)) {
            if (!notificationRepository.existsByMessageAndOrder("Giao hàng thành công", order)) {
                notification.setCustomer(customer);
                notification.setMessage("Giao hàng thành công");
                notification.setOrder(order);

                notificationRepository.save(notification);
            }
        }

        return orderRepository.save(order);
    }

    @Override
    public void destroyOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        orderDetailRepository.deleteAllByOrder(order);

        orderRepository.delete(order);
    }

    @Override
    public List<Order> getCurrentOrdersByStatus(OrderStatusEnum orderStatusEnum) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        Customer customer = customerRepository
                .findCustomerByUserName(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return orderRepository.findAllByCustomerAndStatus(customer, orderStatusEnum);
    }

    @Override
    public Order getOrderByCode(String code) {
        return orderRepository.findByOrderCode(code);
    }

    @Override
    public List<Order> autoUpdateOrdersStatus() {
        List<Order> orders = getCurrentOrdersByStatus(OrderStatusEnum.WAITING_DELIVERY);

        orders.forEach(item -> {
            if (LocalDate.now().equals(item.getDeliveryDate().plusDays(3))) {
                updateOrderStatus(item.getId(), OrderStatusEnum.CANCEL);

                if (!notificationRepository.existsByMessageAndOrder("Đã huỷ", item)) {
                    Notification notification = new Notification();

                    notification.setMessage("Đã huỷ");
                    notification.setOrder(item);
                    notification.setCustomer(item.getCustomer());

                    notificationRepository.save(notification);
                }

            }
        });
        return orders;
    }

    //Generate order code
    static final String AB = "0123456789";
    static SecureRandom rnd = new SecureRandom();

    private String randomString() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
