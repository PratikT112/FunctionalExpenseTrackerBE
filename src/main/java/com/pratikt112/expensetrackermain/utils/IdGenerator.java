package com.pratikt112.expensetrackermain.utils;

import com.pratikt112.expensetrackermain.repository.ExpenseRepo;
import com.pratikt112.expensetrackermain.repository.ReconRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdGenerator {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HHmmssSSS");

    private final ExpenseRepo expenseRepo;
    private final ReconRepo reconRepo;

    public String generateExpenseId(UUID userId) {
        String userPrefix = userId.toString().replace("-", "").substring(0, 8);
        for (int attempt = 0; attempt < 5; attempt++) {
            LocalDateTime now = LocalDateTime.now();
            String id = userPrefix + "-" + now.format(DATE_FMT) + "-" + now.format(TIME_FMT);
            if (!expenseRepo.existsById(id)) return id;
            try { Thread.sleep(1); } catch (InterruptedException ignored) {}
        }
        throw new IllegalStateException("Failed to generate unique ExpenseId after 5 attempts");
    }

    public String generateReconId(String expenseId) {
        for (int attempt = 0; attempt < 5; attempt++) {
            LocalDateTime now = LocalDateTime.now();
            String id = expenseId + "-R-" + now.format(TIME_FMT);
            if (!reconRepo.existsById(id)) return id;
            try { Thread.sleep(1); } catch (InterruptedException ignored) {}
        }
        throw new IllegalStateException("Failed to generate unique ReconId after 5 attempts");
    }

    public String generateProratedExpenseId(UUID userId) {
        String userPrefix = userId.toString().replace("-", "").substring(0, 8);
        for (int attempt = 0; attempt < 5; attempt++) {
            LocalDateTime now = LocalDateTime.now();
            String id = "P" + userPrefix + "-" + now.format(DATE_FMT) + "-" + now.format(TIME_FMT);
            if (!expenseRepo.existsById(id)) return id;
            try { Thread.sleep(1); } catch (InterruptedException ignored) {}
        }
        throw new IllegalStateException("Failed to generate unique ExpenseId after 5 attempts");
    }

}
