package com.hust.vitech.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.vitech.Enum.GenderEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public class Person extends BaseModel{
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

    public Person(String userName, String password, String email){
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public Person(String userName, String password, String email, GenderEnum genderEnum, String address){
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.genderEnum = genderEnum;
        this.address = address;
    }
}
