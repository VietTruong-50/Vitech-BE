package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.CartItem;
import com.hust.vitech.Model.Product;
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
    public ShoppingSession createShoppingSession() {
        ShoppingSession shoppingSession = new ShoppingSession();
        return shoppingSessionRepository.save(shoppingSession);
    }

    @Override
    public ShoppingSession addItemToCart(CartItemRequest cartItemRequest) {
        ShoppingSession shoppingSession = null;

        CartItem cartItem = cartItemRepository
                .findByProductIdAndShoppingSessionId(cartItemRequest.getProductId(), cartItemRequest.getShoppingSessionId())
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItemRequest.getQuantity() + cartItem.getQuantity());

            cartItemRepository.save(cartItem);
        } else {
            cartItem = new CartItem();
            Product product = productRepository.findById(cartItemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (!shoppingSessionRepository
                    .existsById(cartItemRequest.getShoppingSessionId())) {
                shoppingSession = createShoppingSession();
            } else {
                shoppingSession = shoppingSessionRepository
                        .findById(cartItemRequest.getShoppingSessionId()).orElse(null);
            }

            cartItem.setProduct(product);
            cartItem.setShoppingSession(shoppingSession);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setItemPrice(product.getActualPrice());

            cartItemRepository.save(cartItem);
        }
        return shoppingSession;
    }

    @Override
    public void updateCart(Long currentSessionId, Long itemId, int quantity) {

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
    public Double getTotalValues(Long shoppingSessionId) {
        Set<CartItem> cartItems = cartItemRepository.findAllByShoppingSessionId(shoppingSessionId);

        double sum = 0d;

        for (CartItem c : cartItems) {
            sum += c.getProduct().getActualPrice() * c.getQuantity();
        }

        return sum;
    }
}
