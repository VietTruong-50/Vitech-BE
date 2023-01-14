package com.hust.vitech.Repository;

import com.hust.vitech.Model.ShoppingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingSessionRepository extends JpaRepository<ShoppingSession, Long> {

    @Query("SELECT SUM(p.actualPrice * c.quantity) FROM CartItem c " +
            "JOIN Product p ON p.id = c.product.id " +
            "JOIN ShoppingSession s ON s.id = c.shoppingSession.id " +
            "WHERE c.shoppingSession.id = ?1")
    Double getTotalValues(Long shoppingSessionId);

    @Query(value = "select * from shoppingSession s " +
            "join customers c on s.id = c.shopping_session_id " +
            "where c.user_name = ?1", nativeQuery = true)
    ShoppingSession findByCustomerUserName(String userName);
}
