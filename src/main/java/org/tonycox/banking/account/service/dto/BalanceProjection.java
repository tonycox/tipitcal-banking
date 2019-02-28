package org.tonycox.banking.account.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BalanceProjection {
    private BigDecimal amount;
}
