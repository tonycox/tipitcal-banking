package org.tonycox.banking.account.service;

import org.tonycox.banking.model.AccountEvent;
import org.tonycox.banking.model.BalanceProjection;
import org.tonycox.banking.request.AccountEventRequest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public interface AccountService {
    Boolean addEvent(AccountEventRequest event);

    Stream<AccountEvent> getAllEvents(Long userId, LocalDateTime dateTime);

    Stream<AccountEvent> getAllEvents(Long userId);

    BalanceProjection reduceEventsToBalance(Long userId);

    void deleteAll();
}
