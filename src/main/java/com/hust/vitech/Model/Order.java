package com.hust.vitech.Model;

import com.hust.vitech.Enum.OrderStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String orderCode;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Double total;

    private OrderStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "shipping_id")
    private ShippingMethod shippingMethod;

    @OneToMany(mappedBy = "order")
    private Set<OrderDetail> orderDetails;
}
