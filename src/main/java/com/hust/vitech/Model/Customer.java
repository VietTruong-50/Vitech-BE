package com.hust.vitech.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hust.vitech.Enum.GenderEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends Person{

    private String role;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "wishlist_products",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> wishListProducts;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_session_id", referencedColumnName = "id")
    @JsonIgnore
    private ShoppingSession shoppingSession;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private Set<Comment> comments;
    public Customer(String userName, String password, String email,
                    GenderEnum genderEnum, String address, String role, String fullName, String phone, LocalDate dateOfBirth){
        super(userName, password, email, genderEnum, address, fullName, phone, dateOfBirth);
        this.role = role;
    }

}
