package app.api.request;

import app.api.mapper.AccountMapper;
import app.api.response.AccountResponse;
import app.document.Account;
import app.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {
    @Autowired
    private WebTestClient webClient;

    @Autowired
    private  AccountRepository accountRepository;
    @Autowired
    private AccountMapper accountMapper;

    @AfterEach
     void cleanUp(){
        accountRepository.deleteAll().block();
    }

    @Test
    void createAccount() {
        AccountRequest accountRequest = AccountRequest.builder().accountId(UUID.randomUUID()).documentNumber("146898").build();
        webClient.post().uri("/api/accounts")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(accountRequest),AccountRequest.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(AccountResponse.class)
                .value(AccountResponse::getDocumentNumber,equalTo("146898"));
    }

    @Test
    void getAccount() {
        AccountRequest accountRequest = AccountRequest.builder().accountId(UUID.randomUUID()).documentNumber("356898").build();
        Account account = accountMapper.toAccount(accountRequest);
        account = accountRepository.save(account).block();

        webClient.get().uri("/api/accounts/{id}",account.getAccountId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(AccountResponse.class)
                .value(AccountResponse::getDocumentNumber,equalTo("356898"));
    }
}
