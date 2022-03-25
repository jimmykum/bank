package app.repository;

import app.document.OperationsType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationsTypeRepository extends ReactiveMongoRepository<OperationsType, String> {
}
