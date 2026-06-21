package com.pratikt112.expensetrackermain.service;

import com.pratikt112.expensetrackermain.DTO.ResourceDTOs;
import com.pratikt112.expensetrackermain.model.PaymentMethod;
import com.pratikt112.expensetrackermain.model.User;
import com.pratikt112.expensetrackermain.repository.PaymentMethodRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepo paymentMethodRepo;

    public PaymentMethodService(PaymentMethodRepo paymentMethodRepo) {
        this.paymentMethodRepo = paymentMethodRepo;
    }

    public List<ResourceDTOs.PaymentMethodResponse> getPaymentMethods(User user) {
        return paymentMethodRepo.findByUserAndActiveTrue(user).stream()
                .map(p -> ResourceDTOs.PaymentMethodResponse.builder()
                        .paymentId(p.getPaymentId().toString())
                        .paymentName(p.getPaymentName())
                        .type(p.getType())
                        .active(p.isActive())
                        .build())
                .collect(Collectors.toList());
    }

    public ResourceDTOs.PaymentMethodResponse addPaymentMethod(User user, ResourceDTOs.PaymentMethodRequest req) {
        if (paymentMethodRepo.existsByUserAndPaymentName(user, req.getPaymentName())) {
            throw new RuntimeException("Payment method with this name already exists");
        }

        PaymentMethod pm = PaymentMethod.builder()
                .paymentName(req.getPaymentName())
                .type(req.getType())
                .active(true)
                .user(user)
                .build();

        pm = paymentMethodRepo.save(pm);

        return ResourceDTOs.PaymentMethodResponse.builder()
                .paymentId(pm.getPaymentId().toString())
                .paymentName(pm.getPaymentName())
                .type(pm.getType())
                .active(pm.isActive())
                .build();
    }

    public void deletePaymentMethod(User user, UUID paymentId) {
        PaymentMethod pm = paymentMethodRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));
        if (!pm.getUser().getId().equals(user.getId())) throw new RuntimeException("Access denied");
        pm.setActive(false);  // soft delete - keep for historical expense references
        paymentMethodRepo.save(pm);
    }
}
