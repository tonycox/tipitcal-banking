package org.tonycox.banking.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class StatementDto {
    private String operationType;
    private String amount;
    private LocalDateTime date;
}
