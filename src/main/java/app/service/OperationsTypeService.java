package app.service;

import app.document.OperationsType;

public interface OperationsTypeService {
    boolean isOperationsTypeDefined();
    void createOperationsType(OperationsType operationsType);
}
