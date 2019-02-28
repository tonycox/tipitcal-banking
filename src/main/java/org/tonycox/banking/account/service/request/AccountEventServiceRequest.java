package org.tonycox.banking.account.service.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.tonycox.banking.account.model.AccountEventType;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class AccountEventServiceRequest {
    private Long userId;
    private BigDecimal amount;
    private AccountEventType eventType;
}
