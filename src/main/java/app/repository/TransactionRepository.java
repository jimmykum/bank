package app.repository;

import app.document.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, UUID> {

    @Query("{'accountId' : ?0}")
    Flux<Transaction> findAllByAccountIdOrderByEventDate(UUID accountId);

    @Query("{'accountId' : ?0 ,'amount': { $lt : '0' }, 'balance': {$ne : '0.0'} }")
    Flux<Transaction> findAllUnBalancedByAccountId(UUID accountId, Sort sort);
}
