package org.tonycox.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tonycox.banking.model.AccountEvent;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Repository
public interface AccountEventRepository extends JpaRepository<AccountEvent, Long> {
    Stream<AccountEvent> findAllByUserIdAndCreatedAtLessThan(Long userId, LocalDateTime createdAt);
}
