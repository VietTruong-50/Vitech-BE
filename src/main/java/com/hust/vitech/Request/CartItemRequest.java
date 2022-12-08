package com.hust.vitech.Request;

import lombok.Data;

@Data
public class CartItemRequest {

    private Long productId;

    private int quantity;

    private Long shoppingSessionId;
}
