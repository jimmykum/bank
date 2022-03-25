package app.service;

import app.document.Account;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AccountService {
    Mono<Account>  createAccount(Mono<Account> account);
    Mono<Account>  getAccount(UUID accountId);
    Mono<Account> findByDocumentNumber(String documentNumber);
}
