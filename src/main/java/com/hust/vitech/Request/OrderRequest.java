package com.hust.vitech.Request;

import com.hust.vitech.Enum.OrderStatusEnum;
import lombok.Data;

@Data
public class OrderRequest {

    private Long shippingMethodId;

    private OrderStatusEnum orderStatusEnum;

}
