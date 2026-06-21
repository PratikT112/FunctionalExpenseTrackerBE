package com.pratikt112.expensetrackermain.model;

import com.pratikt112.expensetrackermain.enums.ExpenseNecessity;
import com.pratikt112.expensetrackermain.enums.ExpenseType;
import com.pratikt112.expensetrackermain.enums.ReconciliationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "EXPENSE")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Expense {

    @Id
    @Column(name = "EXPENSE_ID", nullable = false)
    private String id;

    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;

    @Column(name = "AMOUNT", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "RECON_AMOUNT", nullable = false, precision = 12, scale = 2)
    private BigDecimal reconciledAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private ExpenseCategory category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PAYMENT_METHOD_ID")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "EXP_NECESSITY", nullable = false)
    private ExpenseNecessity necessity;

    @Enumerated(EnumType.STRING)
    @Column(name = "EXP_TYPE", nullable = false)
    private ExpenseType expenseType;

    @Enumerated(EnumType.STRING)
    @Column(name = "RECON_STATUS", nullable = false)
    private ReconciliationStatus reconciliationStatus;

    @Column(name = "EXP_DT", nullable = false)
    private LocalDate expenseDate;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean deleted = false;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
        if (this.reconciledAmount == null) this.reconciledAmount = BigDecimal.ZERO;
        if (this.reconciliationStatus == null) this.reconciliationStatus = ReconciliationStatus.UNRECONCILED;
        if (this.expenseType == null) this.expenseType = ExpenseType.PERSONAL;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
