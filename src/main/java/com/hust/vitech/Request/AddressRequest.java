package com.hust.vitech.Request;

import lombok.Data;

@Data
public class AddressRequest {

    private String receiverName;

    private String phone;

    private String email;

    private String city;

    private String district;

    private String  subDistrict;

    private String specificAddress;

    private boolean isLevant;

}
