package org.tonycox.banking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tonycox.banking.model.AccountEvent;
import org.tonycox.banking.model.AccountEventType;
import org.tonycox.banking.model.BalanceProjection;
import org.tonycox.banking.repository.AccountEventRepository;
import org.tonycox.banking.request.AccountEventRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
    private final AccountEventRepository repository;
    private final ValidationUtil validator;

    public Boolean addEvent(AccountEventRequest event) {
        validator.validateUser(event);
        validator.validateAmount(event, () -> reduceEventsToBalance(event.getUserId()));
        AccountEvent model = new AccountEvent()
                .setUserId(event.getUserId())
                .setEventType(event.getEventType())
                .setAmount(event.getAmount());
        repository.save(model);
        return true;
    }

    public Stream<AccountEvent> getAllEvents(Long userId, LocalDateTime dateTime) {
        return repository.findAllByUserIdAndCreatedAtLessThan(userId, dateTime);
    }

    public Stream<AccountEvent> getAllEvents(Long userId) {
        List<AccountEvent> collect = getAllEvents(userId, LocalDateTime.MIN).collect(Collectors.toList());
        return collect.stream();
    }

    // todo add snapshoting
    public BalanceProjection reduceEventsToBalance(Long userId) {
        return getAllEvents(userId)
                .map(event -> {
                    BalanceProjection projection = new BalanceProjection();
                    if (event.getEventType() == AccountEventType.DEPOSIT) {
                        projection.setAmount(event.getAmount());
                    }
                    if (event.getEventType() == AccountEventType.WITHDRAW) {
                        projection.setAmount(event.getAmount().negate());
                    }
                    return projection;
                })
                .reduce(new BalanceProjection().setAmount(new BigDecimal(0D)),
                        (left, right) -> new BalanceProjection()
                                .setAmount(left.getAmount().add(right.getAmount())));
    }

    public void clearAll() {
        repository.deleteAll();
    }
}
