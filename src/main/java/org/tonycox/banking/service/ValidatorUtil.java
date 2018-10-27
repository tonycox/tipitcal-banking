package org.tonycox.banking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tonycox.banking.model.AccountEventType;
import org.tonycox.banking.model.BalanceProjection;
import org.tonycox.banking.repository.UserRepository;
import org.tonycox.banking.request.AccountEventRequest;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ValidatorUtil {
    private final UserRepository userRepository;

    public void validateUser(AccountEventRequest event) {
        if (!userRepository.existsById(event.getUserId())) {
            throw new RuntimeException("there is no such user");
        }
    }

    public void validateAmount(AccountEventRequest event, Supplier<BalanceProjection> sup) {
        Double requestedAmount = event.getAmount();
        if (requestedAmount <= 0) {
            throw new RuntimeException("amount must be grater than 0");
        }
        if (event.getEventType() == AccountEventType.WITHDRAW) {
            BalanceProjection balance = sup.get();
            if (balance.getAmount() < requestedAmount) {
                throw new RuntimeException("requested amount is greater than balance");
            }
        }
    }
}
