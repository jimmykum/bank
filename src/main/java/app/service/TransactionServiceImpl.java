package app.service;

import app.document.Transaction;
import app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final OperationsTypeService operationsTypeService;

    @Override
    public Mono<Transaction> createTransaction(Transaction transaction) {
        return
                Mono.just(transaction)
                        .zipWith(operationsTypeService.getMultiplier(transaction.getOperationTypeId()).subscribeOn(Schedulers.boundedElastic()))
                        .map(tuple -> {
                            tuple.getT1().setAmount(tuple.getT1().getAmount().multiply(BigDecimal.valueOf(tuple.getT2())));
                            tuple.getT1().setTransactionId(UUID.randomUUID());
                            tuple.getT1().setEventDate(Instant.now());
                            return tuple.getT1();
                        })
                        .flatMap(transactionRepository::save);

    }

    @Override
    public Flux<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    private Mono<Transaction> toTransaction(Integer val, Transaction transaction) {
        System.out.println("***mapper");
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setEventDate(Instant.now());
        transaction.setAmount(transaction.getAmount().multiply(BigDecimal.valueOf(val)));
        return Mono.just(transaction);
    }
}
