package com.hust.vitech.Request;

import com.hust.vitech.Enum.OrderStatusEnum;
import com.hust.vitech.Enum.PaymentMethodEnum;
import lombok.Data;

@Data
public class OrderRequest {

    private Long shippingMethodId;

    private PaymentMethodEnum paymentMethodEnum;

    private String cardNumber;

}
