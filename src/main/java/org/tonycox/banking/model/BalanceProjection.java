package org.tonycox.banking.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BalanceProjection {
    private Double amount;
}
