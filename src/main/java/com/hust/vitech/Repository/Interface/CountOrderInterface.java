package com.hust.vitech.Repository.Interface;

import com.hust.vitech.Enum.OrderStatusEnum;

public interface CountOrderInterface {

     OrderStatusEnum getStatus();

     int getQuantity();

     double getTotalAll();
}
