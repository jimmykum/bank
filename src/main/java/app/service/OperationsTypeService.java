package app.service;

import app.document.OperationsType;
import reactor.core.publisher.Mono;

public interface OperationsTypeService {
    boolean isOperationsTypeDefined();
    void createOperationsType(OperationsType operationsType);
    Mono<Integer> getMultiplier(String operation_type_id);

}
