package com.pratikt112.expensetrackermain.repository;

import com.pratikt112.expensetrackermain.model.ExpenseRecon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReconRepo extends JpaRepository<ExpenseRecon, String> {
    List<ExpenseRecon> findByExpense_Id(String expenseId);
    boolean existsById(String id);
}
