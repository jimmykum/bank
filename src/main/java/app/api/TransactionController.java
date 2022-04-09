package app.api;

import app.api.mapper.TransactionMapper;
import app.api.request.TransactionRequest;
import app.api.response.TransactionResponse;
import app.exception.BadInputException;
import app.exception.EntityNotFoundException;
import app.service.OperationsTypeService;
import app.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;
    private final OperationsTypeService operationsTypeService;

    @Operation(summary = "Create a new Transaction")
    @PostMapping()
    public Mono<TransactionResponse> createTransaction(@RequestBody @Valid Mono<TransactionRequest> transactionRequest) {
        return transactionRequest.map(transactionMapper::toTransaction)
                .flatMap(transactionService::createTransaction)
                .flatMap(transactionService::balance)
                .map(transactionMapper::toTransactionResponse)
                .onErrorResume(ex -> Mono.error(new BadInputException(ex.getMessage())));
    }

    @Operation(summary = "Get Account's all Transactions")
    @GetMapping("/account/{id}")
    public Flux<TransactionResponse> getAccountsAllTransaction(@PathVariable("id") String id) {
        return Mono.just(id)
                .map(UUID::fromString)
                .flatMapMany(transactionService::getAccountsAllTransaction)
                .map(transactionMapper::toTransactionResponse)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Transactions")));
    }

    @Operation(summary = "Get all Transactions")
    @GetMapping()
    public Flux<TransactionResponse> getAllTransaction() {
       return transactionService.getAllTransaction()
               .map(transactionMapper::toTransactionResponse)
               .switchIfEmpty(Mono.error(new EntityNotFoundException("Transactions")));
    }

}
