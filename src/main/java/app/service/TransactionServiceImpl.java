package app.service;

import app.document.Transaction;
import app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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
                            tuple.getT1().setBalance( tuple.getT1().getAmount());
                            return tuple.getT1();
                        })
                        .flatMap(transactionRepository::save);

    }

    @Override
    public Mono<Transaction> balance(Transaction transaction) {
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            List<Transaction> listOfModifiedTransactions = new ArrayList<>();
            BigDecimal creditVal = transaction.getBalance();
            AtomicReference<BigDecimal> cVal = new AtomicReference<>(creditVal);
            Sort sort = Sort.by(Sort.Order.asc("eventDate"));
            Flux<Transaction> allUnbalancedTransaction = transactionRepository.findAllUnBalancedByAccountId(transaction.getAccountId(), sort);
            return allUnbalancedTransaction.map(trns -> {
                        BigDecimal pendingCredit = cVal.get();
                        if (pendingCredit.compareTo(BigDecimal.ZERO) == 0) {
                            return trns;
                        }
                        if (pendingCredit.compareTo(BigDecimal.ZERO) > 0) {
                            BigDecimal initialBalance = trns.getBalance();
                            if (initialBalance.abs().compareTo(pendingCredit) < 0) {
                                trns.setBalance(BigDecimal.ZERO);
                                pendingCredit = pendingCredit.add(initialBalance);

                            } else {
                                trns.setBalance(initialBalance.add(pendingCredit));
                                pendingCredit = BigDecimal.ZERO;
                            }
                            cVal.set(pendingCredit);
                            listOfModifiedTransactions.add(trns);
                        }
                        return trns;
                    }).
                    collectList().thenReturn(listOfModifiedTransactions)
                    .map(items -> {
                        transaction.setBalance(cVal.get());
                        items.add(transaction);
                        return items;
                    })
                    .flatMapMany(Flux::just)
                    .flatMap(lst -> Flux.fromIterable(lst).flatMap(transactionRepository::save))
                    .filterWhen(transaction1 -> {
                                if (transaction1.getTransactionId().equals(transaction.getTransactionId())) {
                                    return Mono.just(true);
                                }
                                return Mono.just(false);
                            }
                    ).single();
        }
        return Mono.just(transaction);
    }

    @Override
    public Flux<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    @Override
    public Flux<Transaction> getAccountsAllTransaction(UUID accountId) {
        return transactionRepository.findAllByAccountIdOrderByEventDate(accountId);
    }


    private Flux<Transaction> filterByOperations(UUID accountId,List<Integer> opsId) {
        return transactionRepository.findAllByAccountIdOrderByEventDate(accountId)
                .filter(transaction ->  opsId.contains(Integer.valueOf(transaction.getOperationTypeId())));

    }



    private Mono<Transaction> toTransaction(Integer val, Transaction transaction) {
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setEventDate(Instant.now());
        transaction.setAmount(transaction.getAmount().multiply(BigDecimal.valueOf(val)));
        return Mono.just(transaction);
    }
}
