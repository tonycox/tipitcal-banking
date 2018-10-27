package org.tonycox.banking.dto;

import lombok.Value;

import java.util.List;

@Value
public class StatementDto {
    private List<String> statements;
}
