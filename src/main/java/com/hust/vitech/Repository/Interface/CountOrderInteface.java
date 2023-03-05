package com.hust.vitech.Repository.Interface;

import com.hust.vitech.Enum.OrderStatusEnum;

public interface CountOrderInteface {

     OrderStatusEnum getStatus();

     int getQuantity();

     double getTotalAll();
}
