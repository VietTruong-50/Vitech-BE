package com.hust.vitech.Service;

import com.hust.vitech.Model.ShoppingSession;
import com.hust.vitech.Request.CartItemRequest;

public interface CartService {
    void addItemToCart(CartItemRequest cartItemRequest) throws Exception;
    void removeItemFromCart(Long currentSessionId, Long itemId);
    ShoppingSession getShoppingCart(Long id);
    Long getTotalValues(Long shoppingSessionId);
}
