package app.api;

import app.api.mapper.AccountMapper;
import app.api.request.AccountRequest;
import app.api.response.AccountResponse;
import app.exception.EntityNotFoundException;
import app.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    @Operation(summary = "Create a new Account")
    @PostMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AccountResponse> createAccount(@RequestBody @Valid Mono<AccountRequest> accountRequest) {
        return accountRequest.map(req -> Mono.just(accountMapper.toAccount(req)))
                .flatMap(accountService::createAccount)
                .map(accountMapper::toAccountResponse);
                //.map(acc -> ResponseEntity.status(HttpStatus.OK).body(accountMapper.toAccountResponse(acc)));
    }

    @Operation(summary = "Get an account by providing account_id")
    @GetMapping("/accounts/{id}")
    public Mono<ResponseEntity<AccountResponse>> getAccount(@PathVariable String id) {
           return Mono.just(UUID.fromString(id))
                    .flatMap(accountService::getAccount)
                   .map(accountMapper::toAccountResponse)
                   .map(res -> ResponseEntity.status(HttpStatus.OK).body(res))
                   .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all Accounts")
    @GetMapping("/accounts")
    public Flux<AccountResponse> findByDocumentNumber(@RequestParam Optional<String> document_number) {
        return
                Mono.just(document_number.orElse(""))
                        .flatMapMany(doc -> {
                                    if (StringUtils.hasText(doc)) {
                                        return accountService.findByDocumentNumber(doc);
                                    } else {
                                        return accountService.findAllAccounts();
                                    }
                                }
                        )
                        .map(accountMapper::toAccountResponse)
                        .switchIfEmpty(Mono.error(new EntityNotFoundException("Account")));
    }
}
