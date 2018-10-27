package org.tonycox.banking.request;

import lombok.Data;
import org.tonycox.banking.model.AccountEventType;

@Data
public class AccountEventRequest {
    private Long userId;
    private Double amount;
    private AccountEventType eventType;
}
