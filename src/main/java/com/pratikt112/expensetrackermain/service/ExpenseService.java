package com.pratikt112.expensetrackermain.service;

import com.pratikt112.expensetrackermain.DTO.ExpenseAmendDto;
import com.pratikt112.expensetrackermain.DTO.ExpenseDTO;
import com.pratikt112.expensetrackermain.DTO.ExpenseResponseDTO;
import com.pratikt112.expensetrackermain.enums.ReconciliationStatus;
import com.pratikt112.expensetrackermain.model.*;
import com.pratikt112.expensetrackermain.repository.*;
import com.pratikt112.expensetrackermain.utils.IdGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepo expenseRepo;
    private final ExpCategoryRepo expCategoryRepo;
    private final PaymentMethodRepo paymentMethodRepo;
    private final ReconRepo reconRepo;
    private final IdGenerator idGenerator;

    public ExpenseService(ExpenseRepo expenseRepo, ExpCategoryRepo expCategoryRepo,
                          PaymentMethodRepo paymentMethodRepo, ReconRepo reconRepo,
                          IdGenerator idGenerator) {
        this.expenseRepo = expenseRepo;
        this.expCategoryRepo = expCategoryRepo;
        this.paymentMethodRepo = paymentMethodRepo;
        this.reconRepo = reconRepo;
        this.idGenerator = idGenerator;
    }

    public List<ExpenseResponseDTO> getExpensesForMonth(User user, int year, int month) {
        List<Expense> expenses = expenseRepo.findActiveByUserAndMonth(user.getId(), year, month);
        // Batch load recons for all expenses to avoid N+1
        List<String> expenseIds = expenses.stream().map(Expense::getId).collect(Collectors.toList());
        Map<String, List<ExpenseRecon>> reconsByExpense = reconRepo.findAll().stream()
                .filter(r -> expenseIds.contains(r.getExpense().getId()))
                .collect(Collectors.groupingBy(r -> r.getExpense().getId()));

        return expenses.stream()
                .map(e -> ExpenseResponseDTO.from(e, reconsByExpense.getOrDefault(e.getId(), List.of())))
                .collect(Collectors.toList());
    }

    @Transactional
    public ExpenseResponseDTO addExpense(User user, ExpenseDTO dto) {
        ExpenseCategory category = resolveCategory(user, dto.getCategoryName());
        PaymentMethod pm = dto.getPaymentMethodId() != null
                ? paymentMethodRepo.findById(dto.getPaymentMethodId()).orElse(null)
                : null;

        Expense expense = Expense.builder()
                .id(idGenerator.generateExpenseId(user.getId()))
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .category(category)
                .paymentMethod(pm)
                .necessity(dto.getNecessity())
                .expenseType(dto.getExpenseType())
                .expenseDate(dto.getExpDt())
                .user(user)
                .build();

        expense = expenseRepo.save(expense);
        return ExpenseResponseDTO.from(expense, List.of());
    }

    @Transactional
    public ExpenseResponseDTO amendExpense(User user, String expenseId, ExpenseAmendDto dto) {
        Expense expense = expenseRepo.findByIdAndDeletedFalse(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found: " + expenseId));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        if (dto.getDescription() != null) expense.setDescription(dto.getDescription());
        if (dto.getAmount() != null) expense.setAmount(dto.getAmount());
        if (dto.getNecessity() != null) expense.setNecessity(dto.getNecessity());
        if (dto.getExpenseType() != null) expense.setExpenseType(dto.getExpenseType());
        if (dto.getExpDt() != null) expense.setExpenseDate(dto.getExpDt());

        if (dto.getCategoryName() != null) {
            expense.setCategory(resolveCategory(user, dto.getCategoryName()));
        }
        if (dto.getPaymentMethodId() != null) {
            paymentMethodRepo.findById(dto.getPaymentMethodId()).ifPresent(expense::setPaymentMethod);
        }

        expense = expenseRepo.save(expense);
        List<ExpenseRecon> recons = reconRepo.findByExpense_Id(expense.getId());
        return ExpenseResponseDTO.from(expense, recons);
    }

    @Transactional
    public void deleteExpense(User user, String expenseId) {
        Expense expense = expenseRepo.findByIdAndDeletedFalse(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found: " + expenseId));
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        expense.setDeleted(true);
        expenseRepo.save(expense);
    }

    // ── Reconciliation ────────────────────────────────────────────────────────

    @Transactional
    public ExpenseResponseDTO addRecon(User user, String expenseId,
                                       java.math.BigDecimal reconAmount, String reconDesc) {
        Expense expense = expenseRepo.findByIdAndDeletedFalse(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found: " + expenseId));
        if (!expense.getUser().getId().equals(user.getId())) throw new RuntimeException("Access denied");

        java.math.BigDecimal newTotal = expense.getReconciledAmount().add(reconAmount);
        if (newTotal.compareTo(expense.getAmount()) > 0) {
            throw new RuntimeException("Recon amount exceeds expense amount");
        }

        expense.setReconciledAmount(newTotal);
        expense.setReconciliationStatus(
                newTotal.compareTo(expense.getAmount()) == 0
                        ? ReconciliationStatus.FULLY_RECONCILED
                        : ReconciliationStatus.PARTIALLY_RECONCILED
        );

        ExpenseRecon recon = ExpenseRecon.builder()
                .id(idGenerator.generateReconId(expenseId))
                .expense(expense)
                .reconAmount(reconAmount)
                .reconDesc(reconDesc)
                .reconDate(java.time.LocalDate.now())
                .user(user)
                .build();

        expenseRepo.save(expense);
        reconRepo.save(recon);

        List<ExpenseRecon> recons = reconRepo.findByExpense_Id(expenseId);
        return ExpenseResponseDTO.from(expense, recons);
    }

    @Transactional
    public ExpenseResponseDTO removeRecon(User user, String expenseId, String reconId) {
        Expense expense = expenseRepo.findByIdAndDeletedFalse(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!expense.getUser().getId().equals(user.getId())) throw new RuntimeException("Access denied");

        ExpenseRecon recon = reconRepo.findById(reconId)
                .orElseThrow(() -> new RuntimeException("Recon not found: " + reconId));

        java.math.BigDecimal newTotal = expense.getReconciledAmount().subtract(recon.getReconAmount());
        expense.setReconciledAmount(newTotal);
        expense.setReconciliationStatus(
                newTotal.compareTo(java.math.BigDecimal.ZERO) == 0
                        ? ReconciliationStatus.UNRECONCILED
                        : ReconciliationStatus.PARTIALLY_RECONCILED
        );

        reconRepo.delete(recon);
        expenseRepo.save(expense);

        List<ExpenseRecon> recons = reconRepo.findByExpense_Id(expenseId);
        return ExpenseResponseDTO.from(expense, recons);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private ExpenseCategory resolveCategory(User user, String categoryName) {
        ExpenseCategory cat = expCategoryRepo.getExpenseCategoryByUserId_IdAndCategoryName(user.getId(), categoryName);
        if (cat != null) return cat;
        cat = expCategoryRepo.getExpenseCategoryByCategoryNameAndDefaultCategory(categoryName, true);
        if (cat != null) return cat;
        throw new RuntimeException("Category not found: " + categoryName);
    }
}
