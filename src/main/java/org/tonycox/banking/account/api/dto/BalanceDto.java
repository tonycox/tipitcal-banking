package org.tonycox.banking.account.api.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class BalanceDto {
    private BigDecimal amount;
}
