package com.pratikt112.expensetrackermain.service;

import com.pratikt112.expensetrackermain.DTO.ResourceDTOs;
import com.pratikt112.expensetrackermain.enums.CategoryStatus;
import com.pratikt112.expensetrackermain.model.ExpenseCategory;
import com.pratikt112.expensetrackermain.model.User;
import com.pratikt112.expensetrackermain.repository.ExpCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final ExpCategoryRepo expCategoryRepo;

    public CategoryService(ExpCategoryRepo expCategoryRepo) {
        this.expCategoryRepo = expCategoryRepo;
    }

    public List<ResourceDTOs.CategoryResponse> getCategories(User user) {
        return expCategoryRepo.findByUserIdOrDefaultCategoryTrue(user).stream()
                .map(c -> ResourceDTOs.CategoryResponse.builder()
                        .categoryId(c.getCategoryId().toString())
                        .categoryName(c.getCategoryName())
                        .defaultCategory(c.isDefaultCategory())
                        .status(c.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    public ResourceDTOs.CategoryResponse addCategory(User user, ResourceDTOs.CategoryRequest req) {
        if (expCategoryRepo.existsByCategoryNameAndDefaultCategory(req.getCategoryName(), true)) {
            throw new RuntimeException("A default category with this name already exists");
        }
        if (expCategoryRepo.existsByUserIdAndCategoryName(user, req.getCategoryName())) {
            throw new RuntimeException("You already have a category with this name");
        }

        ExpenseCategory cat = ExpenseCategory.builder()
                .categoryName(req.getCategoryName())
                .userId(user)
                .defaultCategory(false)
                .status(CategoryStatus.ACTIVE)
                .build();

        cat = expCategoryRepo.save(cat);

        return ResourceDTOs.CategoryResponse.builder()
                .categoryId(cat.getCategoryId().toString())
                .categoryName(cat.getCategoryName())
                .defaultCategory(cat.isDefaultCategory())
                .status(cat.getStatus())
                .build();
    }

    public void deleteCategory(User user, UUID categoryId) {
        ExpenseCategory cat = expCategoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if (cat.isDefaultCategory()) throw new RuntimeException("Cannot delete a default category");
        if (!cat.getUserId().getId().equals(user.getId())) throw new RuntimeException("Access denied");
        expCategoryRepo.delete(cat);
    }
}
