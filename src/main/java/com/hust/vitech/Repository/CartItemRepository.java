package com.hust.vitech.Repository;

import com.hust.vitech.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByProductIdAndShoppingSessionId(Long itemId, Long shoppingSessionId);
    Set<CartItem> findAllByShoppingSessionId(Long shoppingSessionId);

    void deleteAllByShoppingSessionId(Long shoppingSessionId);

}
