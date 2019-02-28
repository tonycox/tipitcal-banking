package org.tonycox.banking.account.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tonycox.banking.account.api.request.AccountEventRequest;
import org.tonycox.banking.account.service.request.AccountEventServiceRequest;
import org.tonycox.banking.auth.repository.UserRepository;
import org.tonycox.banking.core.exception.ValidationException;
import org.tonycox.banking.account.model.AccountEventType;
import org.tonycox.banking.account.service.dto.BalanceProjection;

import java.math.BigDecimal;
import java.util.function.Supplier;

@Service
@Transactional
@RequiredArgsConstructor
public class ValidationUtil {
    private final UserRepository userRepository;

    public void validateUser(AccountEventServiceRequest event) {
        if (!userRepository.existsById(event.getUserId())) {
            throw new ValidationException("there is no such user");
        }
    }

    public void validateAmount(AccountEventServiceRequest event, Supplier<BalanceProjection> sup) {
        BigDecimal requestedAmount = event.getAmount();
        if (requestedAmount.compareTo(new BigDecimal(0)) <= 0) {
            throw new ValidationException("amount must be grater than 0");
        }
        if (event.getEventType() == AccountEventType.WITHDRAW) {
            BalanceProjection balance = sup.get();
            if (balance.getAmount().compareTo(requestedAmount) < 0) {
                throw new ValidationException("requested amount is greater than balance");
            }
        }
    }
}
