package app.service;

import app.document.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<Transaction> createTransaction(Mono<Transaction> transaction);
    Flux<Transaction> getAllTransaction();
}
