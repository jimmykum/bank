package app.repository;

import app.document.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

@DataMongoTest
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

    @Test
    void testFindAccountByDocumentNumber() {
        UUID pk = UUID.randomUUID();
        String documentNumber = "1357";
        Account account = Account.builder().accountId(pk).documentNumber(documentNumber).build();

        Flux<Account> accountFlux = accountRepository.save(account)
                .flatMapMany(acc -> accountRepository.findAllByDocumentNumber(acc.getDocumentNumber()));

        StepVerifier.create (accountFlux)
                .expectNextMatches(acc -> acc.getDocumentNumber().equals(documentNumber))
                .verifyComplete();
    }
}
