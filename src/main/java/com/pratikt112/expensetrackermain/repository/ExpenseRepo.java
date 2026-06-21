package com.pratikt112.expensetrackermain.repository;

import com.pratikt112.expensetrackermain.model.Expense;
import com.pratikt112.expensetrackermain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, String> {

    // Fetch non-deleted expenses for a user in a given month/year
    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId " +
           "AND e.deleted = false " +
           "AND YEAR(e.expenseDate) = :year " +
           "AND MONTH(e.expenseDate) = :month " +
           "ORDER BY e.expenseDate DESC")
    List<Expense> findActiveByUserAndMonth(
            @Param("userId") UUID userId,
            @Param("year") int year,
            @Param("month") int month);

    // For soft-delete check - find only non-deleted
    Optional<Expense> findByIdAndDeletedFalse(String id);

    boolean existsById(String id);
}
