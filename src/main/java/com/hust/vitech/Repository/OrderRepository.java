package com.hust.vitech.Repository;

import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer(Customer customer);

    Order findByOrderCode(String orderCode);
}
