package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Product;
import com.hust.vitech.Model.User;
import com.hust.vitech.Repository.ProductRepository;
import com.hust.vitech.Repository.UserRepository;
import com.hust.vitech.Service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class WishlistServiceImpl implements WishlistService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addToWishlist(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.findUserByUserName(authentication.getName()).ifPresent(user -> {
            Optional<Product> product = productRepository.findById(productId);

            if (product.isPresent()) {
                user.getWishListProducts().add(product.get());

                product.get().getUsers().add(user);

                userRepository.save(user);
            }
        });
    }

    @Override
    public void removeFromWishlist(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.findUserByUserName(authentication.getName()).ifPresent(user -> {
            Optional<Product> product = productRepository.findById(productId);

            if (product.isPresent()) {
                user.getWishListProducts().remove(product.get());

                product.get().getUsers().remove(user);

                userRepository.save(user);
            }
        });
    }

    @Override
    public Set<Product> getWishlist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findUserByUserName(authentication.getName());

        return user.map(User::getWishListProducts).orElse(null);
    }
}
