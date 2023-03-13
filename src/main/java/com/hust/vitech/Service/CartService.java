package com.hust.vitech.Service;

import com.hust.vitech.Model.CartItem;
import com.hust.vitech.Model.ShoppingSession;
import com.hust.vitech.Request.CartItemRequest;

import java.util.List;

public interface CartService {
    ShoppingSession addItemToCart(CartItemRequest cartItemRequest) throws Exception;

    List<CartItem> updateCurrentCart(List<CartItem> cartItemList);

    void removeItemFromCart(Long itemId);

    ShoppingSession getShoppingCart();

    Double getTotalValues(Long shoppingSessionId);

    ShoppingSession createShoppingSession();

}
