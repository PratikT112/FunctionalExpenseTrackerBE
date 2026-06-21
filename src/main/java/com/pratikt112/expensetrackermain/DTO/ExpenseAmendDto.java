package com.pratikt112.expensetrackermain.DTO;

import com.pratikt112.expensetrackermain.enums.ExpenseNecessity;
import com.pratikt112.expensetrackermain.enums.ExpenseType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExpenseAmendDto {
    private String description;
    private BigDecimal amount;
    private ExpenseNecessity necessity;
    private ExpenseType expenseType;
    private LocalDate expDt;
    private String categoryName;
    private UUID paymentMethodId;
}
