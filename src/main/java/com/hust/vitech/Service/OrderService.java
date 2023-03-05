package com.hust.vitech.Service;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Model.Order;
import com.hust.vitech.Request.OrderRequest;
import com.hust.vitech.Repository.Interface.CountOrderInteface;
import com.hust.vitech.Response.CountOrderResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest);
    Order updateOrderStatus(Long orderId, OrderStatusEnum statusEnum);
    Order destroyOrder(Long orderId);

    Page<Order> getCurrentOrdersByStatus(OrderStatusEnum orderStatusEnum, int page, int size, String sortBy);

    Order getOrderByCode(String code);

    Page<Order> getAllOrders(int page, int size, String sortBy);

    List<Order> autoUpdateOrdersStatus();

    Order updateOrder(Long orderId, OrderRequest orderRequest);

    Page<Order> searchOrdersByOrderCode(String orderCode, int page, int size, String sortBy);

    Page<Order> statisticSuccessOrderAndOrderDateBetween(LocalDate startDate, LocalDate endDate, int page, int size);

    List<CountOrderResponse> statisticCountOrder();

}
