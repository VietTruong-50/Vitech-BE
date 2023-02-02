package com.hust.vitech.Repository;

import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.Notification;
import com.hust.vitech.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByCustomer(Customer customer);

    boolean existsByMessageAndOrder(String message, Order order);
}
