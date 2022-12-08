package com.hust.vitech.Service;

import com.hust.vitech.Model.Product;

import java.util.Set;

public interface WishlistService {
    void addToWishlist(Long productId);

    void removeFromWishlist(Long productId);

    Set<Product> getWishlist();

}
