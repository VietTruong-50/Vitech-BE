package com.hust.vitech.Service.Impl;

import com.hust.vitech.Model.Customer;
import com.hust.vitech.Model.Product;
import com.hust.vitech.Model.User;
import com.hust.vitech.Repository.CustomerRepository;
import com.hust.vitech.Repository.ProductRepository;
import com.hust.vitech.Repository.UserRepository;
import com.hust.vitech.Service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class WishlistServiceImpl implements WishlistService {
    @Autowired
    private CustomerRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addToWishlist(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.findCustomerByUserName(authentication.getName()).ifPresent(user -> {
            Optional<Product> product = productRepository.findById(productId);

            if (product.isPresent()) {
                user.getWishListProducts().add(product.get());

                product.get().getCustomers().add(user);

                userRepository.save(user);
            }
        });
    }

    @Override
    public void removeFromWishlist(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.findCustomerByUserName(authentication.getName()).ifPresent(user -> {
            Optional<Product> product = productRepository.findById(productId);

            if (product.isPresent()) {
                user.getWishListProducts().remove(product.get());

                product.get().getCustomers().remove(user);

                userRepository.save(user);
            }
        });
    }

    @Override
    public List<Product> getWishlist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Customer> user = userRepository.findCustomerByUserName(authentication.getName());

        return user.map(Customer::getWishListProducts).orElse(null);
    }
}
