package com.pratikt112.expensetrackermain.repository;

import com.pratikt112.expensetrackermain.model.ExpenseCategory;
import com.pratikt112.expensetrackermain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpCategoryRepo extends JpaRepository<ExpenseCategory, UUID> {

    // All categories accessible to a user = their own + all defaults
    List<ExpenseCategory> findByUserIdOrDefaultCategoryTrue(User userId);

    boolean existsByCategoryNameAndDefaultCategory(String categoryName, boolean defaultCategory);
    boolean existsByUserIdAndCategoryName(User userId, String categoryName);

    ExpenseCategory getExpenseCategoryByCategoryNameAndDefaultCategory(String name, boolean defaultCategory);
    ExpenseCategory getExpenseCategoryByUserId_IdAndCategoryName(UUID userId, String name);
}
