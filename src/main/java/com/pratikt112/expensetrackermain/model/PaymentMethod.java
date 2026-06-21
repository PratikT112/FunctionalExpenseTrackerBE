package com.pratikt112.expensetrackermain.model;

import com.pratikt112.expensetrackermain.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "PAYMENT_METHOD")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PAYMENT_ID")
    private UUID paymentId;

    @Column(name = "PAYMENT_NAME", nullable = false, length = 50)
    private String paymentName;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_TYPE", nullable = false)
    private PaymentType type;

    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
}
