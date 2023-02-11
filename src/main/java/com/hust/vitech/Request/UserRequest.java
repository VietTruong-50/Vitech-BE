package com.hust.vitech.Request;

import com.hust.vitech.Enum.GenderEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class UserRequest {
    private String userName;

    private String password;

    private String fullName;

    private String email;

    private GenderEnum genderEnum;

    private String phone;

    private LocalDate dateOfBirth;

    private List<String> roles;

    private boolean isCustomer;

}
