package com.hust.vitech.Repository;

import com.hust.vitech.Model.Order;
import com.hust.vitech.Model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Transactional
    void deleteAllByOrder(Order order);
}
