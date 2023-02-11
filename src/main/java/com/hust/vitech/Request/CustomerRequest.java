package com.hust.vitech.Request;

import com.hust.vitech.Enum.GenderEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerRequest {


    private String fullName;

    private String email;

    private GenderEnum genderEnum;

    private String phone;

    private LocalDate dateOfBirth;


    private Long addressId;

    private AddressRequest addressRequest;

}
