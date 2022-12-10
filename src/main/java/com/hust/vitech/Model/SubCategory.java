package com.hust.vitech.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "sub_categories")
public class SubCategory extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sub_cate_name", unique = true)
    private String subCateName;

    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "subCategory")
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}