package org.mav.example.payments.api.dto;

import java.math.BigDecimal;
import java.util.Map;

public record BalanceSummaryDto(
        Long userId,
        BigDecimal totalBalance,
        Map<String, BigDecimal> byType
) {}