package org.tonycox.banking.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.tonycox.banking.model.AccountEventType;

@Data
@Accessors(chain = true)
public class AccountEventRequest {
    private Long userId;
    private Double amount;
    private AccountEventType eventType;
}
