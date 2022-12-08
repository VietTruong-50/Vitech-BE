package com.hust.vitech.Model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private Double orderItemPrice;

    public OrderDetail(Order order, Product product, int quantity, double orderItemPrice){
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.orderItemPrice = orderItemPrice;
    }
}
