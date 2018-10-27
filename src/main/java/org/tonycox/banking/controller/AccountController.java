package org.tonycox.banking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.tonycox.banking.dto.BalanceDto;
import org.tonycox.banking.dto.StatementDto;
import org.tonycox.banking.model.BalanceProjection;
import org.tonycox.banking.request.AccountEventRequest;
import org.tonycox.banking.service.AccountService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Transactional
public class AccountController {

    private final AccountService service;

    @PostMapping("/event")
    public ResponseEntity handleEvent(@RequestBody AccountEventRequest event) {
        return Optional.of(service.addEvent(event))
                .map(bool -> new ResponseEntity<>(HttpStatus.NO_CONTENT))
                .orElseThrow(RuntimeException::new);
    }

    @GetMapping("/{userId}/balance")
    public BalanceDto getBalance(@PathVariable Long userId) {
        BalanceProjection projection = service.reduceEventsToBalance(userId);
        return new BalanceDto(projection.getAmount());
    }

    @GetMapping("/{userId}/statement")
    public List<StatementDto> getStatement(@PathVariable Long userId) {
        return service.getAllEvents(userId)
                .map(event -> new StatementDto(event.getEventType().name(), event.getAmount().toString(), event.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
