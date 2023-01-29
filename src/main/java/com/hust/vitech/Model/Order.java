package com.hust.vitech.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.vitech.Enum.OrderStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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

    private LocalDate deliverDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate orderDate;

    @ManyToOne
    @JoinColumn(name = "shipping_id")
    private ShippingMethod shippingMethod;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
}
