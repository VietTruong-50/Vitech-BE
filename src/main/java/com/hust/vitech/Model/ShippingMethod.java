package com.hust.vitech.Model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "shipping_methods")
public class ShippingMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String shippingMethod;

    private Double price;
}
