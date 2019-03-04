package org.tonycox.banking.account;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tonycox.banking.AbstractTest;
import org.tonycox.banking.account.api.dto.BalanceDto;
import org.tonycox.banking.account.api.dto.StatementDto;
import org.tonycox.banking.account.model.AccountEventDao;
import org.tonycox.banking.account.model.AccountEventType;
import org.tonycox.banking.account.api.request.AccountEventRequest;
import org.tonycox.banking.account.repository.AccountEventRepository;
import org.tonycox.banking.auth.service.AuthService;
import org.tonycox.banking.auth.service.dto.SignedUser;
import org.tonycox.banking.auth.service.request.SignUpServiceRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AccountITest extends AbstractTest {
    @Autowired
    private AuthService userService;
    @Autowired
    private AccountEventRepository accountEventRepository;
    private SignedUser user;

    @BeforeEach
    void setUp() {
        user = userService.signUp(new SignUpServiceRequest().setEmail("test@mail.com").setPassword(""));
    }

    @AfterEach
    void tearDown() {
        userService.removeUser(user.getId());
        accountEventRepository.deleteAll();
    }

    @Test
    void cannotDepositZero() {
        AccountEventRequest request = deposit(BigDecimal.ZERO);
        ResponseEntity<Object> entity = rest.postForEntity("/account/event", request, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void cannotDepositSmallAmount() {
        AccountEventRequest request = deposit(BigDecimal.valueOf(0.001));
        ResponseEntity<Object> entity = rest.postForEntity("/account/event", request, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void cannotWithdrawIfLessBalance() {
        AccountEventRequest request = withdraw(BigDecimal.TEN);
        ResponseEntity<Object> entity = rest.postForEntity("/account/event", request, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void concurrencyWithdraw() throws Exception {
        accountEventRepository.save(daoDeposit(BigDecimal.valueOf(100)));

        ForkJoinPool commonPool = new ForkJoinPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        AccountEventRequest request = withdraw(new BigDecimal(20));
        Stream.<Runnable>generate(() -> () -> {
            rest.postForObject("/account/event", request, Object.class);
            latch.countDown();
        }).limit(10).parallel().forEach(commonPool::execute);
        latch.await();
        ResponseEntity<BalanceDto> entity = rest.getForEntity("/account/{userId}/balance",
                BalanceDto.class, user.getId());
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
        BalanceDto body = entity.getBody();
        assertNotNull(body);
        BigDecimal amount = body.getAmount();
        assertEquals(BigDecimal.ZERO, amount.setScale(0, 0));
    }

    @Test
    @SuppressWarnings("unchecked")
    void returnedStatementsAreActual() {
        accountEventRepository.save(daoDeposit(BigDecimal.TEN));
        accountEventRepository.save(daoDeposit(BigDecimal.valueOf(5)).setEventType(AccountEventType.WITHDRAW));

        ResponseEntity<List> entity = rest.getForEntity("/account/{userId}/statement", List.class, user.getId());
        List list = Objects.requireNonNull(entity.getBody());
        List<StatementDto> statements = (List<StatementDto>) list.stream()
                .map(item -> objectMapper.convertValue(item, StatementDto.class))
                .collect(Collectors.toList());
        assertEquals(2, statements.size());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    private AccountEventDao daoDeposit(BigDecimal amount) {
        return new AccountEventDao()
                .setUserId(user.getId())
                .setAmount(amount)
                .setEventType(AccountEventType.DEPOSIT);
    }

    private AccountEventRequest deposit(BigDecimal amount) {
        return new AccountEventRequest()
                .setAmount(amount)
                .setEventType(AccountEventType.DEPOSIT)
                .setUserId(user.getId());
    }

    private AccountEventRequest withdraw(BigDecimal amount) {
        return new AccountEventRequest()
                .setAmount(amount)
                .setEventType(AccountEventType.WITHDRAW)
                .setUserId(user.getId());
    }
}
