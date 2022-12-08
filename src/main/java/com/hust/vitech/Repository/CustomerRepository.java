package com.hust.vitech.Repository;

import com.hust.vitech.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByUserName(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);
}
