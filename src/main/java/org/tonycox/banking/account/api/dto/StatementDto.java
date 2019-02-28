package org.tonycox.banking.account.api.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class StatementDto {
    private String operationType;
    private BigDecimal amount;
    private LocalDateTime date;
}
