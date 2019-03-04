package org.tonycox.banking.account.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tonycox.banking.account.model.AccountEventDao;
import org.tonycox.banking.account.model.AccountEventType;
import org.tonycox.banking.account.service.dto.AccountEvent;
import org.tonycox.banking.account.service.dto.BalanceProjection;
import org.tonycox.banking.account.repository.AccountEventRepository;
import org.tonycox.banking.account.service.request.AccountEventServiceRequest;
import org.tonycox.banking.account.service.util.ValidationUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {
    private final AccountEventRepository repository;
    private final ValidationUtil validator;
    private final ModelMapper mapper;

    public Boolean addEvent(AccountEventServiceRequest event) {
        validator.validateUser(event);
        validator.validateAmount(event, () -> reduceEventsToBalance(event.getUserId()));
        AccountEventDao model = new AccountEventDao()
                .setUserId(event.getUserId())
                .setEventType(event.getEventType())
                .setAmount(event.getAmount());
        repository.save(model);
        return true;
    }

    public Stream<AccountEvent> getAllEvents(Long userId, LocalDateTime dateTime) {
        validator.validateUser(userId);
        return repository.findAllByUserIdAndCreatedAtLessThan(userId, dateTime)
                .map(event -> mapper.map(event, AccountEvent.class))
                .collect(Collectors.toList())
                .stream();
    }

    public Stream<AccountEvent> getAllEvents(Long userId) {
        validator.validateUser(userId);
        return repository.findAllByUserId(userId)
                .map(event -> mapper.map(event, AccountEvent.class))
                .collect(Collectors.toList())
                .stream();
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

    public void deleteAll() {
        repository.deleteAll();
    }
}
