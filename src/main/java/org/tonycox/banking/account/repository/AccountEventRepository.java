package org.tonycox.banking.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.tonycox.banking.account.model.AccountEventDao;

import javax.persistence.LockModeType;
import java.util.stream.Stream;

@Repository
public interface AccountEventRepository extends JpaRepository<AccountEventDao, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Stream<AccountEventDao> findAllByUserId(Long userId);
}
