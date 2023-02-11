package com.hust.vitech.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class StatisticQuantityResponse {

    private int productQuantity;

    private int customerQuantity;

    private int orderQuantity;
}
