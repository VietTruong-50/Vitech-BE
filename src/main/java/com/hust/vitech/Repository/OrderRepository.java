package com.hust.vitech.Repository;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByCustomerAndStatus(Pageable pageable, Customer customer, OrderStatusEnum orderStatusEnum);

    Order findByOrderCode(String orderCode);
}
