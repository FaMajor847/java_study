package org.mav.example.payments.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_gen")
    @SequenceGenerator(name = "payment_gen", sequenceName = "payment_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "external_id", length = 64)
    private String externalId;


    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment payment)) return false;
        // Для JPA сущностей часто сравнивают по id (если уже присвоен)
        return id != null && id.equals(payment.id);
    }

    @Override
    public int hashCode() {
        return 31; // стабильный hash для JPA сущностей
    }

    @Override
    public String toString() {
        return "Payment{" + "id=" + id + ", productId=" + productId + ", userId=" + userId + ", amount=" + amount + ", status=" + status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }

}
