package app.repository;

import app.document.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
public class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void testCreateAndVerifyAccount() {
        UUID pk = UUID.randomUUID();
        String documentNumber = "12345";
        Account account = Account.builder().accountId(pk).documentNumber(documentNumber).build();

        Mono<Account> accountMono = accountRepository.save(account)
                .flatMap(acc -> accountRepository.findById(acc.getAccountId()));

        StepVerifier.create(accountMono)
                .expectNextMatches(acc -> acc.getDocumentNumber().equals(documentNumber))
                .verifyComplete();
    }
}
