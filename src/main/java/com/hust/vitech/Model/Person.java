package com.hust.vitech.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.vitech.Enum.GenderEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

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

    private String fullName;

    @Column(name = "email")
    private String email;

    private GenderEnum genderEnum;

    private String address;

    private String phone;

    private LocalDate dateOfBirth;

    public Person(String userName, String password, String email){
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public Person(String userName, String password, String email, GenderEnum genderEnum, String address, String fullName, String phone, LocalDate dateOfBirth){
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.genderEnum = genderEnum;
        this.address = address;
        this.fullName = fullName;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
    }
}
