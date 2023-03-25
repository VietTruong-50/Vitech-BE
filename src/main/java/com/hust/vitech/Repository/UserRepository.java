package com.hust.vitech.Repository;

import com.hust.vitech.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserName(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    @Query("select u from User u join u.roles r where r.name = :role")
    Page<User> findAllByRole(String role, Pageable pageable);
    @Query(value = "select sum(total) from orders " +
            "where status = 2 AND YEAR(CURDATE()) " +
            "AND MONTH(order_date) = ?1 ORDER BY MONTH(order_date)", nativeQuery = true)
    Double getTotalValueByMonth(int month);

    @Query(value = "select count(*) from orders where status = ?1 AND YEAR(CURDATE())", nativeQuery = true)
    int getOrderByStatusInYear(int status);
}
