package org.tonycox.banking.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class BalanceDto {
    private BigDecimal amount;
}
