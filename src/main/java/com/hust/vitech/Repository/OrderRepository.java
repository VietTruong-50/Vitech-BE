package com.hust.vitech.Repository;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.Order;
import com.hust.vitech.Repository.Interface.CountOrderInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByCustomerAndStatus(Pageable pageable, Customer customer, OrderStatusEnum orderStatusEnum);

    Page<Order> findAllByStatus(Pageable pageable, OrderStatusEnum orderStatusEnum);

    Order findByOrderCode(String orderCode);

    Page<Order> findAllByOrderCodeContaining(Pageable pageable, String orderCode);

    @Query(value = "select * from orders where status = 2 and (order_date >= ?1 AND order_date <= ?2)", nativeQuery = true)
    List<Order> statisticSuccessOrderAndOrderDateBetween(LocalDate startDate, LocalDate endDate);

    @Query(value = "select status, count(*) as quantity, sum(total) as totalAll from orders group by status order by status ASC", nativeQuery = true)
    List<CountOrderInterface> statisticCountOrder();
}
