package com.hust.vitech.Request;

import com.hust.vitech.Enum.GenderEnum;
import lombok.Data;

@Data
public class CustomerRequest {


    private String userName;

    private String password;


    private String email;

    private GenderEnum genderEnum;

    private String address;

    private String role;
}
