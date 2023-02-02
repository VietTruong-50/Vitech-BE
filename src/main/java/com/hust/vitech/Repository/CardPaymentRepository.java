package com.hust.vitech.Repository;

import com.hust.vitech.Model.CardPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardPaymentRepository extends JpaRepository<CardPayment, Long> {
    Optional<CardPayment> findByCardNumber(String cardNumber);

}
