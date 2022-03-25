package app.repository;

import app.document.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, UUID> {
}
