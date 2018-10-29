package org.tonycox.banking.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.tonycox.banking.model.AccountEventType;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class AccountEventRequest {
    private Long userId;
    private BigDecimal amount;
    private AccountEventType eventType;
}
