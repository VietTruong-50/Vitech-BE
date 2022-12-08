package com.hust.vitech.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "products")
//@SQLDelete(sql = "UPDATE products SET deleted = true WHERE id = ?")
//@Where(clause = "deleted=false")
public class Product extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    @Column(length = 1000)
    private String productCode;

    private String parameters;

    private String content;

    private int quantity;

    @Column(name = "actual_price")
    private Double actualPrice;

    @Column(name = "discount_price")
    private Double discountPrice;

    @Column(name = "feature_image_name")
    private String featureImageName;

    @Column(length = 500000)
    private byte[] featureImageByte;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "product_images",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private Set<ImageModel> productImages = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "wishListProducts")
    private Set<User> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<CartItem> cartItems;
}
