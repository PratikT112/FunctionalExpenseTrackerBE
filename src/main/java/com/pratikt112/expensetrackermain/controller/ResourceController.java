package com.pratikt112.expensetrackermain.controller;

import com.pratikt112.expensetrackermain.DTO.ResourceDTOs;
import com.pratikt112.expensetrackermain.model.User;
import com.pratikt112.expensetrackermain.service.CategoryService;
import com.pratikt112.expensetrackermain.service.PaymentMethodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ResourceController {

    private final CategoryService categoryService;
    private final PaymentMethodService paymentMethodService;

    public ResourceController(CategoryService categoryService, PaymentMethodService paymentMethodService) {
        this.categoryService = categoryService;
        this.paymentMethodService = paymentMethodService;
    }

    // ── Categories ────────────────────────────────────────────────────────────

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(categoryService.getCategories(user));
    }

    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(@AuthenticationPrincipal User user,
                                          @RequestBody ResourceDTOs.CategoryRequest req) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(user, req));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@AuthenticationPrincipal User user,
                                             @PathVariable UUID categoryId) {
        try {
            categoryService.deleteCategory(user, categoryId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── Payment Methods ───────────────────────────────────────────────────────

    @GetMapping("/payment-methods")
    public ResponseEntity<?> getPaymentMethods(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(paymentMethodService.getPaymentMethods(user));
    }

    @PostMapping("/payment-methods")
    public ResponseEntity<?> addPaymentMethod(@AuthenticationPrincipal User user,
                                               @RequestBody ResourceDTOs.PaymentMethodRequest req) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentMethodService.addPaymentMethod(user, req));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/payment-methods/{paymentId}")
    public ResponseEntity<?> deletePaymentMethod(@AuthenticationPrincipal User user,
                                                  @PathVariable UUID paymentId) {
        try {
            paymentMethodService.deletePaymentMethod(user, paymentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
