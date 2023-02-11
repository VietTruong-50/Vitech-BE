package com.hust.vitech.Response;

import lombok.Data;

import java.util.List;

@Data
public class StatisticValueResponse {
    List<Double> saleStatistic;

    List<Integer> orderStatistic;
}
