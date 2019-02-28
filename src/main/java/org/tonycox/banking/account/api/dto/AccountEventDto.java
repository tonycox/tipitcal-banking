package org.tonycox.banking.account.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.tonycox.banking.account.model.AccountEventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class AccountEventDto {
    private Long eventId;
    private Long userId;
    private BigDecimal amount;
    private AccountEventType eventType;
    private LocalDateTime createdAt;
}
