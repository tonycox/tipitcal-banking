package org.tonycox.banking.account.service;

import org.tonycox.banking.account.service.dto.AccountEvent;
import org.tonycox.banking.account.service.dto.BalanceProjection;
import org.tonycox.banking.account.service.request.AccountEventServiceRequest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public interface AccountService {
    Boolean addEvent(AccountEventServiceRequest event);

    Stream<AccountEvent> getAllEvents(Long userId);

    BalanceProjection reduceEventsToBalance(Long userId);
}
