package org.tonycox.banking.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BalanceProjection {
    private BigDecimal amount;
}
