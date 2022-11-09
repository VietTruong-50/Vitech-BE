package com.hust.vitech.Model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

//    @JsonIgnore
//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users;
}
