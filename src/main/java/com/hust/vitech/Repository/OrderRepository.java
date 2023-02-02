package com.hust.vitech.Repository;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomerAndStatus(Customer customer, OrderStatusEnum orderStatusEnum);

    Order findByOrderCode(String orderCode);
}
