package app.api.mapper;

import app.api.request.TransactionRequest;
import app.api.response.TransactionResponse;
import app.document.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {

    Transaction toTransaction(TransactionRequest transactionRequest);
    TransactionResponse toTransactionResponse(Transaction transaction);
}
