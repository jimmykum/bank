package app.repository;

import app.document.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;
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
        Transaction transaction = Transaction.builder()
                .transactionId(pk)
                .accountId(accountId)
                .operationTypeId(operationTypeId)
                .amount(amount)
                .build();

        Mono<Transaction> transactionMono = transactionRepository.save(transaction)
                .flatMap(trans -> transactionRepository.findById(pk));

        StepVerifier.create(transactionMono)
                .expectNextMatches(trans -> trans.getOperationTypeId().equals(operationTypeId) &&
                                            trans.getAccountId().equals(accountId) &&
                                            trans.getAmount().equals(amount) )
                .verifyComplete();
    }
}
