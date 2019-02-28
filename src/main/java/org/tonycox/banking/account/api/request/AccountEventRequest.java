package org.tonycox.banking.account.api.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.tonycox.banking.account.model.AccountEventType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class AccountEventRequest {
    @NotNull
    private Long userId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private AccountEventType eventType;
}
