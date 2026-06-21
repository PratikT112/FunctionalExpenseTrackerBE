package com.pratikt112.expensetrackermain.controller;

import com.pratikt112.expensetrackermain.DTO.ExpenseAmendDto;
import com.pratikt112.expensetrackermain.DTO.ExpenseDTO;
import com.pratikt112.expensetrackermain.DTO.ExpenseResponseDTO;
import com.pratikt112.expensetrackermain.DTO.ResourceDTOs;
import com.pratikt112.expensetrackermain.model.User;
import com.pratikt112.expensetrackermain.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int year,
            @RequestParam(defaultValue = "0") int month) {
        if (year == 0) year = LocalDate.now().getYear();
        if (month == 0) month = LocalDate.now().getMonthValue();
        return ResponseEntity.ok(expenseService.getExpensesForMonth(user, year, month));
    }

    @PostMapping
    public ResponseEntity<?> addExpense(@AuthenticationPrincipal User user,
                                         @RequestBody ExpenseDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.addExpense(user, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{expenseId}")
    public ResponseEntity<?> amendExpense(@AuthenticationPrincipal User user,
                                           @PathVariable String expenseId,
                                           @RequestBody ExpenseAmendDto dto) {
        try {
            return ResponseEntity.ok(expenseService.amendExpense(user, expenseId, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpense(@AuthenticationPrincipal User user,
                                            @PathVariable String expenseId) {
        try {
            expenseService.deleteExpense(user, expenseId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{expenseId}/recon")
    public ResponseEntity<?> addRecon(@AuthenticationPrincipal User user,
                                       @PathVariable String expenseId,
                                       @RequestBody ResourceDTOs.ReconRequest req) {
        try {
            return ResponseEntity.ok(expenseService.addRecon(user, expenseId, req.getReconAmount(), req.getReconDesc()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{expenseId}/recon/{reconId}")
    public ResponseEntity<?> removeRecon(@AuthenticationPrincipal User user,
                                          @PathVariable String expenseId,
                                          @PathVariable String reconId) {
        try {
            return ResponseEntity.ok(expenseService.removeRecon(user, expenseId, reconId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
