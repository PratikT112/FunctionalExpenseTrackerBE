package com.pratikt112.expensetrackermain.repository;

import com.pratikt112.expensetrackermain.model.ProratedExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProratedExpenseRepo extends JpaRepository<ProratedExpense, String> {

}
