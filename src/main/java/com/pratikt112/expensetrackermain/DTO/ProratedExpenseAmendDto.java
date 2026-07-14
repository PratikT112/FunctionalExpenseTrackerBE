package com.pratikt112.expensetrackermain.DTO;

import com.pratikt112.expensetrackermain.enums.ExpenseNecessity;
import com.pratikt112.expensetrackermain.enums.ExpenseType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProratedExpenseAmendDto {
    private String description;
    private BigDecimal amount;
    private ExpenseNecessity necessity;
    private ExpenseType expenseType;
    private String categoryName;
    private UUID paymentMethodId;
    private LocalDate expDt;
    private LocalDate validFrom;
    private LocalDate validTill;
}
