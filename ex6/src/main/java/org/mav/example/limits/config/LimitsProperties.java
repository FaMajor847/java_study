package org.mav.example.limits.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "limits")
public class LimitsProperties {
    private BigDecimal defaultAmount = new BigDecimal("5000.00");
    public BigDecimal getDefaultAmount() { return defaultAmount; }
    public void setDefaultAmount(BigDecimal defaultAmount) { this.defaultAmount = defaultAmount; }

}
