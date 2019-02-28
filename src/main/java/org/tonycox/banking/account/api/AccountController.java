package org.tonycox.banking.account.api;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tonycox.banking.account.service.AccountService;
import org.tonycox.banking.account.api.dto.BalanceDto;
import org.tonycox.banking.account.api.dto.StatementDto;
import org.tonycox.banking.account.service.dto.BalanceProjection;
import org.tonycox.banking.account.api.request.AccountEventRequest;
import org.tonycox.banking.account.service.request.AccountEventServiceRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;
    private final ModelMapper mapper;

    @PostMapping("/event")
    public ResponseEntity handleEvent(@RequestBody AccountEventRequest event) {
        return Optional.of(service.addEvent(mapper.map(event, AccountEventServiceRequest.class)))
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
                .map(event -> new StatementDto(event.getEventType().name(), event.getAmount(), event.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
