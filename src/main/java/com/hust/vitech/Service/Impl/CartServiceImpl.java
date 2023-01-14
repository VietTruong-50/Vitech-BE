package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.CartItem;
import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.Product;
import com.hust.vitech.Model.ShoppingSession;
import com.hust.vitech.Repository.CartItemRepository;
import com.hust.vitech.Repository.CustomerRepository;
import com.hust.vitech.Repository.ProductRepository;
import com.hust.vitech.Repository.ShoppingSessionRepository;
import com.hust.vitech.Request.CartItemRequest;
import com.hust.vitech.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingSessionRepository shoppingSessionRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ShoppingSession createShoppingSession() {
        ShoppingSession shoppingSession = new ShoppingSession();
        return shoppingSessionRepository.save(shoppingSession);
    }

    @Override
    public void updateCartItem(Long itemId, int quantity) {

    }

    @Override
    public ShoppingSession addItemToCart(CartItemRequest cartItemRequest) {

        ShoppingSession shoppingSession = getShoppingCart();

        if (shoppingSession != null) {
            CartItem cartItem = cartItemRepository
                    .findByProductIdAndShoppingSessionId(cartItemRequest.getProductId(), shoppingSession.getId())
                    .orElse(null);

            if (cartItem != null) {
                cartItem.setQuantity(cartItemRequest.getQuantity() + cartItem.getQuantity());

                cartItemRepository.save(cartItem);
            } else {
                cartItem = new CartItem();

                Product product = productRepository.findById(cartItemRequest.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

                cartItem.setProduct(product);
                cartItem.setShoppingSession(shoppingSession);
                cartItem.setQuantity(cartItemRequest.getQuantity());
                cartItem.setItemPrice(product.getActualPrice());

                cartItemRepository.save(cartItem);
            }

        }
        return shoppingSession;
    }

    @Override
    public void updateCurrentCart(List<CartItem> cartItemList) {
        ShoppingSession shoppingSession = getShoppingCart();

        if (shoppingSession != null) {
            removeAllItem();

            cartItemList.forEach(item -> {
                item.setProduct(item.getProduct());
                item.setShoppingSession(shoppingSession);
                item.setQuantity(item.getQuantity());
                item.setItemPrice(item.getProduct().getActualPrice());
            });

            cartItemRepository.saveAll(cartItemList);
        }
    }

    @Override
    public void removeItemFromCart(Long itemId) {
        Set<CartItem> cartItems = cartItemRepository.findAllByShoppingSessionId(getShoppingCart().getId());

        cartItems.forEach(item -> {
            if (item.getProduct().getId().equals(itemId)) {
                cartItemRepository.delete(item);
            }
        });
    }

    public void removeAllItem() {
        cartItemRepository.deleteAllByShoppingSessionId(getShoppingCart().getId());
    }

    @Override
    public ShoppingSession getShoppingCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            Optional<Customer> customer = customerRepository.findCustomerByUserName(authentication.getName());
            if (customer.isPresent()) {
                return customer.get().getShoppingSession();
            }
        }
        return null;
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
