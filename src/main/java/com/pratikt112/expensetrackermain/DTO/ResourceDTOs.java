package com.pratikt112.expensetrackermain.DTO;

import com.pratikt112.expensetrackermain.enums.CategoryStatus;
import com.pratikt112.expensetrackermain.enums.PaymentType;
import lombok.*;

public class ResourceDTOs {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class CategoryRequest {
        private String categoryName;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CategoryResponse {
        private String categoryId;
        private String categoryName;
        private boolean defaultCategory;
        private CategoryStatus status;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class PaymentMethodRequest {
        private String paymentName;
        private PaymentType type;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PaymentMethodResponse {
        private String paymentId;
        private String paymentName;
        private PaymentType type;
        private boolean active;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ReconRequest {
        private String expenseId;
        private java.math.BigDecimal reconAmount;
        private String reconDesc;
    }
}
