package com.hust.vitech.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "categories")
public class Category extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @Column(length = 500000)
    private byte[] categoryImageByte;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private Set<Brand> brands = new HashSet<>();
}
