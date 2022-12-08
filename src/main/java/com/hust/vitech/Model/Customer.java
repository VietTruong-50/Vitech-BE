package com.hust.vitech.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.vitech.Enum.GenderEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "email")
    private String email;

    private GenderEnum genderEnum;

    private String address;

    private String role;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "wishlist_products",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> wishListProducts;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_session_id", referencedColumnName = "id")
    @JsonIgnore
    private ShoppingSession shoppingSession;

    public Customer(String userName, String password, String email, GenderEnum genderEnum, String address, String role){
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.genderEnum = genderEnum;
        this.address = address;
        this.role = role;
    }

    public Customer(String userName, String password, String email){
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
