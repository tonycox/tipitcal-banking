package org.tonycox.banking.request;

import lombok.Data;

@Data
public class DepositEvent {
    private Long userId;
    private Double amount;
    private CurrencyCode currencyCode;
}
