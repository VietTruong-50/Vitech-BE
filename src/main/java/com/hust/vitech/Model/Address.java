package com.hust.vitech.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiverName;

    private String phone;

    private String email;

    private String city;

    private String district;

    private String  subDistrict;

    private String specificAddress;

    private boolean isDefault;

    @JsonIgnore
    @OneToMany(mappedBy = "address")
    private List<Order> orders;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
