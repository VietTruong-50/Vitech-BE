package com.hust.vitech.Request;

import lombok.Data;

@Data
public class ShippingMethodRequest {
    private String shippingMethod;

    private double price;
}
