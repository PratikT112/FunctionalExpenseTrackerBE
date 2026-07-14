package com.pratikt112.expensetrackermain.DTO;

import com.pratikt112.expensetrackermain.enums.ExpenseNecessity;
import com.pratikt112.expensetrackermain.enums.ExpenseType;
import com.pratikt112.expensetrackermain.enums.ReconciliationStatus;
import com.pratikt112.expensetrackermain.model.Expense;
import com.pratikt112.expensetrackermain.model.ExpenseRecon;
import com.pratikt112.expensetrackermain.model.ProratedExpense;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProratedExpenseResponseDTO {
    private String proratedExpenseId;
    private String description;
    private BigDecimal amount;
    private String categoryId;
    private String categoryName;
    private UUID paymentMethodId;
    private String paymentMethodName;
    private ExpenseNecessity necessity;
    private ExpenseType expenseType;
    private LocalDate expenseDate;


    /* To be implemented post proration
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ReconEntry {
        private String reconId;
        private BigDecimal reconAmount;
        private String reconDesc;
        private LocalDate reconDate;
    }
     */

    public static ProratedExpenseResponseDTO from(ProratedExpense e) {
        BigDecimal net = e.getAmount();
        return ProratedExpenseResponseDTO.builder()
                .proratedExpenseId(e.getId())
                .description(e.getDescription())
                .amount(e.getAmount())
                .categoryId(e.getCategory().getCategoryId().toString())
                .categoryName(e.getCategory().getCategoryName())
                .paymentMethodId(e.getPaymentMethod() != null ? e.getPaymentMethod().getPaymentId() : null)
                .paymentMethodName(e.getPaymentMethod() != null ? e.getPaymentMethod().getPaymentName() : null)
                .necessity(e.getNecessity())
                .expenseType(e.getExpenseType())
                .expenseDate(e.getExpenseDate())
                .build();
    }
}
