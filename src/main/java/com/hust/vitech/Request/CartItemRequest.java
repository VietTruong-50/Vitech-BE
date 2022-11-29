package com.hust.vitech.Request;

import lombok.Data;

@Data
public class CartItemRequest {

    private Long productId;

    private Long quantity;

    private Long shopping_session_id;
}
