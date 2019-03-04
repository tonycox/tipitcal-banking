package org.tonycox.banking.account.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private static final int AMOUNT_SCALE = 2;
    private final UserRepository userRepository;

    public void validateUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ValidationException("UserDao with id: " + userId + " doesn't exist.");
        }
    }

    public void validateUser(AccountEventServiceRequest event) {
        validateUser(event.getUserId());
    }

    public void validateAmount(AccountEventServiceRequest event, Supplier<BalanceProjection> sup) {
        BigDecimal requestedAmount = event.getAmount();
        if (event.getAmount().scale() > AMOUNT_SCALE) {
            throw new ValidationException("Scale of the amount is bigger than " + AMOUNT_SCALE + " digits after dot.");
        }
        if (requestedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be grater than zero.");
        }
        if (event.getEventType() == AccountEventType.WITHDRAW) {
            BalanceProjection balance = sup.get();
            if (balance.getAmount().compareTo(requestedAmount) < 0) {
                throw new ValidationException("Requested amount is greater than balance.");
            }
        }
    }
}
