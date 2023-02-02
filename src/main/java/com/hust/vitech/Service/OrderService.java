package com.hust.vitech.Service;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Model.Order;
import com.hust.vitech.Request.OrderRequest;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest);
    Order updateOrderStatus(Long orderId, OrderStatusEnum statusEnum);
    void destroyOrder(Long orderId);

    List<Order> getCurrentOrdersByStatus(OrderStatusEnum orderStatusEnum);

    Order getOrderByCode(String code);

    List<Order> autoUpdateOrdersStatus();
}
