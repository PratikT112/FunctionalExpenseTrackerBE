package com.pratikt112.expensetrackermain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "EXPENSE_RECON")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExpenseRecon {

    @Id
    @Column(name = "RECON_ID", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXPENSE_ID", nullable = false)
    private Expense expense;

    @Column(name = "RECON_AMOUNT", nullable = false)
    private BigDecimal reconAmount;

    @Column(name = "RECON_DESC")
    private String reconDesc;

    @Column(name = "RECON_DATE", nullable = false)
    private LocalDate reconDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @PrePersist
    public void prePersist() {
        if (this.reconDate == null) this.reconDate = LocalDate.now();
    }
}
