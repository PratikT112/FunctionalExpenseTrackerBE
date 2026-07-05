package com.pratikt112.expensetrackermain.DTO;

import com.pratikt112.expensetrackermain.enums.ExpenseNecessity;
import com.pratikt112.expensetrackermain.enums.ExpenseType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProratedExpenseDTO {
    private String description;
    private String categoryName;       // category name string, resolved in service
    private UUID paymentMethodId;      // UUID of selected payment method
    private BigDecimal amount;
    private ExpenseNecessity necessity;
    private ExpenseType expenseType;
    private LocalDate expDt;
    private LocalDate validFrom;
    private LocalDate validTill;
}
