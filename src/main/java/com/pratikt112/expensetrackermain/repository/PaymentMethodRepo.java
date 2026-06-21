package com.pratikt112.expensetrackermain.repository;

import com.pratikt112.expensetrackermain.model.PaymentMethod;
import com.pratikt112.expensetrackermain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentMethodRepo extends JpaRepository<PaymentMethod, UUID> {
    List<PaymentMethod> findByUserAndActiveTrue(User user);
    boolean existsByUserAndPaymentName(User user, String paymentName);
}
