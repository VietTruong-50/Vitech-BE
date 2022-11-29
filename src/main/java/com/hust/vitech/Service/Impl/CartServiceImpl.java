package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.CartItem;
import com.hust.vitech.Model.ShoppingSession;
import com.hust.vitech.Repository.CartItemRepository;
import com.hust.vitech.Repository.ProductRepository;
import com.hust.vitech.Repository.ShoppingSessionRepository;
import com.hust.vitech.Request.CartItemRequest;
import com.hust.vitech.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingSessionRepository shoppingSessionRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public void addItemToCart(CartItemRequest cartItemRequest) {

        CartItem cartItem = cartItemRepository
                .findByProductIdAndShoppingSessionId(cartItemRequest.getProductId(), cartItemRequest.getShopping_session_id())
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItemRequest.getQuantity() + cartItem.getQuantity());

            cartItemRepository.save(cartItem);
        } else {
            cartItem = new CartItem();

            cartItem.setShoppingSession(shoppingSessionRepository
                    .findById(cartItemRequest.getShopping_session_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Session not found")));

            cartItem.setProduct(productRepository.findById(cartItemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Session not found")));

            cartItem.setQuantity(cartItemRequest.getQuantity());

            cartItemRepository.save(cartItem);
        }

    }

    @Override
    public void removeItemFromCart(Long currentSessionId, Long itemId) {
        Set<CartItem> cartItems = cartItemRepository.findAllByShoppingSessionId(currentSessionId);

        cartItems.forEach(item -> {
            if (item.getProduct().getId().equals(itemId)) {
                cartItemRepository.delete(item);
            }
        });

    }

    @Override
    public ShoppingSession getShoppingCart(Long id) {
        return shoppingSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
    }

    @Override
    public Long getTotalValues(Long shoppingSessionId) {
        return shoppingSessionRepository.getTotalValues(shoppingSessionId);
    }
}
