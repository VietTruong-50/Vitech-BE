package com.hust.vitech.Service;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Model.Order;
import com.hust.vitech.Request.OrderRequest;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest);
    Order updateOrderStatus(Long orderId, OrderStatusEnum statusEnum);
    void destroyOrder(Long orderId);
}
