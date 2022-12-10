package com.hust.vitech.Request;

import com.hust.vitech.Enum.GenderEnum;
import lombok.Data;

import java.util.List;
@Data
public class UserRequest {
    private String userName;

    private String password;

    private String email;

    private GenderEnum genderEnum;

    private String address;

    private double salary;

    private List<String> roles;

}
