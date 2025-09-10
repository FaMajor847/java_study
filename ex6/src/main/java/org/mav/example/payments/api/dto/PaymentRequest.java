package org.mav.example.payments.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(@NotNull @Min(1) Long userId,
                             @NotNull @Min(1) Long productId,
                             @NotNull @DecimalMin(value = "0,01", inclusive = true) BigDecimal amount
) {
}
