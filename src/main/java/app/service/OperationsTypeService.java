package app.service;

import app.model.OperationsType;

public interface OperationsTypeService {
    boolean isOperationsTypeDefined();
    void createOperationsType(OperationsType operationsType);
}
