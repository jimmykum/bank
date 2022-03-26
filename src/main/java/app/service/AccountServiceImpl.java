package app.service;

import app.document.Account;
import app.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Mono<Account> createAccount(Mono<Account> account) {
        return account.map(acc -> {
                            acc.setAccountId(UUID.randomUUID());
                            return acc;
                        }
                )
                .flatMap(accountRepository::save);
    }

    @Override
    public Mono<Account> getAccount(UUID accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public Flux<Account> findByDocumentNumber(String documentNumber) {
        return  accountRepository.findAllByDocumentNumber(documentNumber);
    }

    @Override
    public Flux<Account> findAllAccounts() {
        return accountRepository.findAll();
    }
}
