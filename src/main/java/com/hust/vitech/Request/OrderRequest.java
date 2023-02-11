package com.hust.vitech.Request;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Enum.PaymentMethodEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderRequest {

    private Long shippingMethodId;

    private PaymentMethodEnum paymentMethodEnum;

    private String cardNumber;

    private Long addressId;

    private OrderStatusEnum orderStatusEnum;
    private LocalDate deliveryDate;

    //    private String receiver;
    private String receiverName;
    private String phone;
    private String email;
    private String city;
    private String district;
    private String subDistrict;
    private String specificAddress;
}
