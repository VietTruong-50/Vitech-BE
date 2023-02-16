package com.hust.vitech.Repository;

import com.hust.vitech.Model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByUserName(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    @Query(value = "select * from customers c where c.user_name LIKE ?1 or c.email like ?1 or c.phone = ?1", nativeQuery = true)
    Page<Customer> findAllByUserNameOrPhoneOrEmailContains(String searchText, Pageable pageable);
}
