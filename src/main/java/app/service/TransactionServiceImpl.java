package app.service;

import app.document.Transaction;
import app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public Mono<Transaction> createTransaction(Mono<Transaction> transaction) {
        return transaction.map(trans -> {
                    trans.setTransactionId(UUID.randomUUID());
                    trans.setEventDate(Instant.now());
                    return trans;
                })
                .flatMap(transactionRepository::save);
    }
}
