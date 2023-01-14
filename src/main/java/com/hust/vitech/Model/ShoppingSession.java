package com.hust.vitech.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shopping_session")
//@SQLDelete(sql = "UPDATE shopping_session SET deleted = true WHERE id = ?")
//@Where(clause = "deleted=false")
public class ShoppingSession extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double total;

    @OneToMany(mappedBy = "shoppingSession")
    private List<CartItem> cartItems;

    @OneToOne(mappedBy = "shoppingSession")
    @JsonIgnore
    private Customer customer;
}
