package org.tonycox.banking.account.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.tonycox.banking.account.model.AccountEventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class AccountEvent {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private AccountEventType eventType;
    private LocalDateTime createdAt;
}
