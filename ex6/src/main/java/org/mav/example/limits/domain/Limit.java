package org.mav.example.limits.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "limits")
public class Limit {
    @Id
    @Min(1)
    @Max(100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "limit_gen")
    @SequenceGenerator(name = "limit_gen", sequenceName = "limit_seq", allocationSize = 1)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "day", nullable = false)
    private LocalDate day;

    @Column(name = "remaining", nullable = false, precision = 19, scale = 2)
    private BigDecimal remaining;

    /** Оптимистическая блокировка при параллельных апдейтах одной строки */
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

}
