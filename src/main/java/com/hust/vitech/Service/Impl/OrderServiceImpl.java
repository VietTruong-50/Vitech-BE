package com.hust.vitech.Service.Impl;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Enum.PaymentMethodEnum;
import com.hust.vitech.Model.*;
import com.hust.vitech.Repository.*;
import com.hust.vitech.Request.OrderRequest;
import com.hust.vitech.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    //Generate order code
    static final String AB = "0123456789";
    static SecureRandom rnd = new SecureRandom();
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
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Order createOrder(OrderRequest orderRequest) {

        CardPayment cardPayment = null;

        Order order = new Order();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        Customer customer = customerRepository.findCustomerByUserName(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Address address = addressRepository.findById(orderRequest.getAddressId()).orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        address.setReceiverName(orderRequest.getReceiverName() != null ? orderRequest.getReceiverName() : customer.getFullName());
        address.setPhone(orderRequest.getPhone() != null ? orderRequest.getPhone() : customer.getPhone());
        address.setEmail(orderRequest.getEmail() != null ? orderRequest.getEmail() : customer.getEmail());

        addressRepository.save(address);

        if (orderRequest.getPaymentMethodEnum() == PaymentMethodEnum.ONLINE_PAYING) {
            cardPayment = cardPaymentRepository.findByCardNumber(orderRequest.getCardNumber()).orElse(null);

            if (cardPayment == null) {
                cardPayment = new CardPayment();

                cardPayment.setCardNumber(orderRequest.getCardNumber());
                cardPayment.setCustomer(customer);

                cardPaymentRepository.save(cardPayment);
            }
        }

        order.setOrderCode(randomString());
        order.setShippingMethod(shippingMethodRepository.findById(orderRequest.getShippingMethodId()).orElse(null));
        order.setCustomer(customer);
        order.setStatus(OrderStatusEnum.WAITING_PROCESS);
        order.setOrderDate(LocalDate.now());
        order.setCardPayment(cardPayment);
        order.setPaymentMethodEnum(orderRequest.getPaymentMethodEnum());
        order.setAddress(address);

        Set<CartItem> cartItems = cartItemRepository.findAllByShoppingSessionId(customer.getShoppingSession().getId());

        if (!cartItems.isEmpty()) {
            cartItems.forEach(item -> {
                Product product = item.getProduct();

                product.setQuantity(product.getQuantity() - item.getQuantity());

                orderRepository.save(order);

                orderDetailRepository.save(new OrderDetail(order, product, item.getQuantity(), item.getProduct().getActualPrice()));

                productRepository.save(product);
            });
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
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(statusEnum);

        Customer customer = customerRepository.findById(order.getCustomer().getId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        String message = "";

        switch (statusEnum) {
            case WAITING_DELIVERY -> {
                order.setDeliveryDate(LocalDate.now().plusDays(2));

                message = "Đặt hàng thành công";
            }
            case SUCCESS -> {
                message = "Giao hàng thành công";
            }
            case CANCEL -> {
                List<OrderDetail> orderDetails = order.getOrderDetails();

                orderDetails.forEach(item -> {
                    Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(null);

                    product.setQuantity(item.getQuantity() + product.getQuantity());

                    productRepository.save(product);
                });

                message = "Đã huỷ";
            }
        }

        Notification notification = notificationRepository.findByOrderId(order.getId());

        if (notification == null) {
            notification = new Notification();
        }

        notification.setCustomer(customer);
        notification.setMessage(message);
        notification.setOrder(order);

//        notificationRepository.save(notification);

        return orderRepository.save(order);
    }

    @Override
    public void destroyOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        orderDetailRepository.deleteAllByOrder(order);

        orderRepository.delete(order);
    }

    @Override
    public Page<Order> getCurrentOrdersByStatus(OrderStatusEnum orderStatusEnum, int page, int size, String sortBy) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Customer customer = customerRepository.findCustomerByUserName(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return orderRepository.findAllByCustomerAndStatus(pageable, customer, orderStatusEnum);
    }

    @Override
    public Order getOrderByCode(String code) {
        return orderRepository.findByOrderCode(code);
    }

    @Override
    public Page<Order> getAllOrders(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return orderRepository.findAll(pageable);
    }

    @Override
    public List<Order> autoUpdateOrdersStatus() {
        List<Order> orders = getCurrentOrdersByStatus(OrderStatusEnum.WAITING_DELIVERY, 0, 100, "orderDate").getContent();
//        orders.forEach(item -> {
//            if (LocalDate.now().equals(item.getDeliveryDate().plusDays(3))) {
//                updateOrderStatus(item.getId(), OrderStatusEnum.CANCEL);

//                if (!notificationRepository.existsByMessageAndOrder("Đã huỷ", item)) {
//                    Notification notification = new Notification();
//
//                    notification.setMessage("Đã huỷ");
//                    notification.setOrder(item);
//                    notification.setCustomer(item.getCustomer());
//
//                    notificationRepository.save(notification);
//                }
//
//            }
//        });
        return orders;
    }

    @Override
    public Order updateOrder(Long orderId, OrderRequest orderRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Address address = addressRepository.findById(orderRequest.getAddressId()).orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        address.setCity(orderRequest.getCity());
        address.setDistrict(orderRequest.getDistrict());
        address.setSpecificAddress(orderRequest.getSpecificAddress());
        address.setSubDistrict(orderRequest.getSubDistrict());

        addressRepository.save(address);

        updateOrderStatus(orderId, orderRequest.getOrderStatusEnum());
        order.setDeliveryDate(orderRequest.getDeliveryDate());

        return order;
    }

    private String randomString() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }


}
