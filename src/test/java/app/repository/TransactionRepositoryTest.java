package app.repository;

import app.document.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

@DataMongoTest
//@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    void createAndVerifyTransactions() {
        UUID pk = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        String operationTypeId = "3";
        BigDecimal amount = BigDecimal.valueOf(12.23);
        BigDecimal balance = BigDecimal.valueOf(12.23);
        Transaction transaction = Transaction.builder()
                .transactionId(pk)
                .accountId(accountId)
                .operationTypeId(operationTypeId)
                .amount(amount)
                .balance(balance)
                .build();

        Mono<Transaction> transactionMono = transactionRepository.save(transaction)
                .flatMap(trans -> transactionRepository.findById(pk));

        StepVerifier.create(transactionMono)
                .expectNextMatches(trans -> trans.getOperationTypeId().equals(operationTypeId) &&
                                            trans.getAccountId().equals(accountId) &&
                                            trans.getBalance().equals(balance) &&
                                            trans.getAmount().equals(amount) )
                .verifyComplete();
    }

    @Test
    void testFindByAccountId(){
        UUID pk = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        Transaction transaction = Transaction.builder()
                .transactionId(pk)
                .accountId(accountId)
                .operationTypeId("3")
                .amount(BigDecimal.valueOf(12.4))
                .balance(BigDecimal.valueOf(10.0))
                .build();
        UUID pk1 = UUID.randomUUID();
        Transaction transaction1 = Transaction.builder()
                .transactionId(pk1)
                .accountId(accountId)
                .operationTypeId("2")
                .amount(BigDecimal.valueOf(5.20))
                .balance(BigDecimal.valueOf(5.20))
                .build();

        transactionRepository.save(transaction1).block();
        transactionRepository.save(transaction).block();

        Flux<Transaction> transactionFlux =  transactionRepository.findAllByAccountIdOrderByEventDate(accountId);

        StepVerifier.create(transactionFlux)
                .expectNext(transaction1,transaction)
                .verifyComplete();
    }

    @Test
    void testFindUnbalancedTransaction() {
        UUID pk = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        Transaction transaction = Transaction.builder()
                .transactionId(pk)
                .accountId(accountId)
                .operationTypeId("3")
                .amount(BigDecimal.valueOf(-12.4))
                .balance(BigDecimal.valueOf(-12.4))
                .build();
        UUID pk1 = UUID.randomUUID();
        Transaction transaction1 = Transaction.builder()
                .transactionId(pk1)
                .accountId(accountId)
                .operationTypeId("2")
                .amount(BigDecimal.valueOf(-15.20))
                .balance(BigDecimal.valueOf(-10.00))
                .build();
        UUID pk3 = UUID.randomUUID();
        Transaction transaction3 = Transaction.builder()
                .transactionId(pk3)
                .accountId(accountId)
                .operationTypeId("1")
                .amount(BigDecimal.valueOf(-10.00))
                .balance(BigDecimal.valueOf(0.00))
                .build();

        UUID pk2 = UUID.randomUUID();
        Transaction transaction2 = Transaction.builder()
                .transactionId(pk2)
                .accountId(accountId)
                .operationTypeId("4")
                .amount(BigDecimal.valueOf(10.0))
                .balance(BigDecimal.valueOf(10.0))
                .build();

        transactionRepository.save(transaction).block();
        transactionRepository.save(transaction1).block();
        transactionRepository.save(transaction3).block();
        transactionRepository.save(transaction2).block();

        Sort sort = Sort.by(Sort.Order.asc("eventDate"));
        Flux<Transaction> transactionFlux =  transactionRepository.findAllUnBalancedByAccountId(accountId,sort);

        StepVerifier.create(transactionFlux)
                .expectNext(transaction,transaction1)
                .verifyComplete();

    }
}
