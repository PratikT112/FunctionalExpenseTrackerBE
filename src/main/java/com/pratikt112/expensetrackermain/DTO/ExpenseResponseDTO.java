package com.pratikt112.expensetrackermain.DTO;

import com.pratikt112.expensetrackermain.enums.ExpenseNecessity;
import com.pratikt112.expensetrackermain.enums.ExpenseType;
import com.pratikt112.expensetrackermain.enums.ReconciliationStatus;
import com.pratikt112.expensetrackermain.model.Expense;
import com.pratikt112.expensetrackermain.model.ExpenseRecon;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExpenseResponseDTO {
    private String expenseId;
    private String description;
    private BigDecimal amount;
    private BigDecimal reconciledAmount;
    private BigDecimal netExpenseAmount;
    private String categoryId;
    private String categoryName;
    private UUID paymentMethodId;
    private String paymentMethodName;
    private ExpenseNecessity necessity;
    private ExpenseType expenseType;
    private ReconciliationStatus reconciliationStatus;
    private LocalDate expenseDate;
    private List<ReconEntry> recons;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ReconEntry {
        private String reconId;
        private BigDecimal reconAmount;
        private String reconDesc;
        private LocalDate reconDate;
    }

    public static ExpenseResponseDTO from(Expense e, List<ExpenseRecon> recons) {
        BigDecimal net = e.getAmount().subtract(e.getReconciledAmount());
        return ExpenseResponseDTO.builder()
                .expenseId(e.getId())
                .description(e.getDescription())
                .amount(e.getAmount())
                .reconciledAmount(e.getReconciledAmount())
                .netExpenseAmount(net)
                .categoryId(e.getCategory().getCategoryId().toString())
                .categoryName(e.getCategory().getCategoryName())
                .paymentMethodId(e.getPaymentMethod() != null ? e.getPaymentMethod().getPaymentId() : null)
                .paymentMethodName(e.getPaymentMethod() != null ? e.getPaymentMethod().getPaymentName() : null)
                .necessity(e.getNecessity())
                .expenseType(e.getExpenseType())
                .reconciliationStatus(e.getReconciliationStatus())
                .expenseDate(e.getExpenseDate())
                .recons(recons.stream().map(r -> ReconEntry.builder()
                        .reconId(r.getId())
                        .reconAmount(r.getReconAmount())
                        .reconDesc(r.getReconDesc())
                        .reconDate(r.getReconDate())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
