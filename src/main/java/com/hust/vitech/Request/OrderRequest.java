package com.hust.vitech.Request;

import com.hust.vitech.Enum.OrderStatusEnum;
import lombok.Data;

@Data
public class OrderRequest {

    private String orderCode;

    private Long shippingMethodId;

    private Long userId;

    private OrderStatusEnum orderStatusEnum;

}
