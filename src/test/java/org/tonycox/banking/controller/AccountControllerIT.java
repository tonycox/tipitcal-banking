package org.tonycox.banking.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tonycox.banking.dto.BalanceDto;
import org.tonycox.banking.dto.ErrorResponseDto;
import org.tonycox.banking.model.AccountEventType;
import org.tonycox.banking.model.User;
import org.tonycox.banking.request.AccountEventRequest;
import org.tonycox.banking.request.SignUpRequest;
import org.tonycox.banking.service.AccountService;
import org.tonycox.banking.service.AuthService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test-h2")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerIT {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate rest;
    @Autowired
    private AuthService userService;
    @Autowired
    private AccountService accountService;
    private User user;

    @BeforeEach
    void setUp() {
        user = userService.signUp(new SignUpRequest().setEmail("test@mail.com").setPassword(""));
    }

    @Test
    void cannotWithdrawIfLessBalance() {
        AccountEventRequest request = withdraw(new BigDecimal(10D));
        ResponseEntity<ErrorResponseDto> entity = rest.postForEntity("http://localhost:" + port + "/banking/v1/account/event",
                request, ErrorResponseDto.class);
        assertEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void concurrencyDepositAndWithdraw() throws InterruptedException {
        BigDecimal expected = new BigDecimal(100D);
        rest.postForEntity("http://localhost:" + port + "/banking/v1/account/event",
                deposit(expected), Object.class);
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        CountDownLatch latch = new CountDownLatch(100);
        List<Boolean> bools = new ArrayList<>(100);
        for (int i = 0; i < 50; i++) bools.add(true);
        for (int i = 0; i < 50; i++) bools.add(false);
        bools.stream().map(bool -> (Runnable) () -> {
            if (bool) {
                rest.postForObject("http://localhost:" + port + "/banking/v1/account/event",
                        deposit(new BigDecimal(1D)), Object.class);
            } else {
                rest.postForObject("http://localhost:" + port + "/banking/v1/account/event",
                        withdraw(new BigDecimal(1D)), Object.class);
            }
            latch.countDown();
        }).parallel().forEach(commonPool::execute);
        latch.await();
        ResponseEntity<BalanceDto> entity = rest.getForEntity("http://localhost:" + port + "/banking/v1/account/{userId}/balance",
                BalanceDto.class, user.getId());
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
        BalanceDto body = entity.getBody();
        assertNotNull(body);
        BigDecimal amount = body.getAmount();
        assertEquals(amount, expected);
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

    @AfterEach
    void tearDown() {
        accountService.clearAll();
    }
}
