package com.hust.vitech.Response;

import com.hust.vitech.Enum.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountOrderResponse {

     private  OrderStatusEnum status;

     private int quantity;

     private double totalAll;
}
