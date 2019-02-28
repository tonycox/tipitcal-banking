package org.tonycox.banking.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.tonycox.banking.model.AccountEvent;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Repository
public interface AccountEventRepository extends JpaRepository<AccountEvent, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    Stream<AccountEvent> findAllByUserIdAndCreatedAtLessThan(Long userId, LocalDateTime createdAt);
}
