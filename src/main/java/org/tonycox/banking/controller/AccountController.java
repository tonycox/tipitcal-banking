package org.tonycox.banking.controller;

import org.springframework.web.bind.annotation.*;
import org.tonycox.banking.dto.BalanceDto;
import org.tonycox.banking.dto.StatementDto;
import org.tonycox.banking.request.DepositEvent;
import org.tonycox.banking.request.WithdrawEvent;

@RestController
@RequestMapping("/account")
public class AccountController {

    @PostMapping("/deposit")
    public void doDepositEvent(@RequestBody DepositEvent event) {

    }

    @PostMapping("/withdraw")
    public void doWithdrawEvent(@RequestBody WithdrawEvent event) {

    }

    @GetMapping("/balance")
    public BalanceDto getBalance() {
        return null;
    }

    @GetMapping("/statement")
    public StatementDto getStatement() {
        return null;
    }
}
