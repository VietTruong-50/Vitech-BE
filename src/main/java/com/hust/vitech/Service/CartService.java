package com.hust.vitech.Service;

import com.hust.vitech.Model.ShoppingSession;
import com.hust.vitech.Request.CartItemRequest;

public interface CartService {
    ShoppingSession addItemToCart(CartItemRequest cartItemRequest) throws Exception;

    void updateCart(Long currentSessionId, Long itemId, int quantity);

    void removeItemFromCart(Long currentSessionId, Long itemId);

    ShoppingSession getShoppingCart(Long id);

    Double getTotalValues(Long shoppingSessionId);

    ShoppingSession createShoppingSession();
}
