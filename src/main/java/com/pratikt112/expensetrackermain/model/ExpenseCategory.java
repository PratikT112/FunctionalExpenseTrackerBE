package com.pratikt112.expensetrackermain.model;

import com.pratikt112.expensetrackermain.enums.CategoryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "EXP_CAT")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExpenseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "CATEGORY_ID", nullable = false)
    private UUID categoryId;

    @Column(name = "CATEGORY_NAME", nullable = false, length = 50)
    private String categoryName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User userId;

    @Column(name = "DEFAULT_CATEGORY", nullable = false)
    private boolean defaultCategory;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private CategoryStatus status;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
