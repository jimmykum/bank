package app.service;

import app.document.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TransactionService {
    Mono<Transaction> createTransaction(Transaction transaction);
    Flux<Transaction> getAllTransaction();
    Flux<Transaction> getAccountsAllTransaction(UUID accountId);
    Mono<Transaction> balance(Transaction transaction) ;
}
