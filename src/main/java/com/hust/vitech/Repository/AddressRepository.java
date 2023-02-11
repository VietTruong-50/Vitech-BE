package com.hust.vitech.Repository;

import com.hust.vitech.Model.Address;
import com.hust.vitech.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a WHERE a.isDefault = true and a.customer.id = ?1")
    Address findByDefaultTrueAndCustomer(Long customerId);
}
