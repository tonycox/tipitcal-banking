package org.tonycox.banking.core.api;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tonycox.banking.account.api.dto.BalanceDto;
import org.tonycox.banking.account.model.AccountEventType;
import org.tonycox.banking.account.api.request.AccountEventRequest;
import org.tonycox.banking.account.service.AccountServiceImpl;
import org.tonycox.banking.auth.model.UserDao;
import org.tonycox.banking.auth.service.AuthService;
import org.tonycox.banking.auth.service.request.SignUpServiceRequest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountControllerIT {
    private static final String HOST = "http://localhost:";

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate rest;
    @Autowired
    private AuthService userService;
    @Autowired
    private AccountServiceImpl accountService;
    private UserDao user;

    @BeforeEach
    void setUp() {
        user = userService.signUp(new SignUpServiceRequest().setEmail("test@mail.com").setPassword(""));
    }

    @AfterEach
    void tearDown() {
        accountService.deleteAll();
    }

    @Test
    void cannotWithdrawIfLessBalance() {
        AccountEventRequest request = withdraw(new BigDecimal(10D));
        ResponseEntity<String> entity = rest.postForEntity(
                HOST + port + "/banking/v1/account/event", request, String.class);
        assertEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void concurrencyWithdraw() throws InterruptedException {
        rest.postForEntity(HOST + port + "/banking/v1/account/event",
                deposit(new BigDecimal(100)), Object.class);
        ForkJoinPool commonPool = new ForkJoinPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        Stream.<Runnable>generate(() -> () -> {
            rest.postForObject(HOST + port + "/banking/v1/account/event",
                    withdraw(new BigDecimal(20)), Object.class);
            latch.countDown();
        }).limit(10).parallel().forEach(commonPool::execute);
        latch.await();
        ResponseEntity<BalanceDto> entity = rest.getForEntity(HOST + port + "/banking/v1/account/{userId}/balance",
                BalanceDto.class, user.getId());
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
        BalanceDto body = entity.getBody();
        assertNotNull(body);
        BigDecimal amount = body.getAmount();
        assertEquals(BigDecimal.ZERO.compareTo(amount), 0);
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
