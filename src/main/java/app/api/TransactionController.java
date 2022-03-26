package app.api;

import app.api.mapper.TransactionMapper;
import app.api.request.TransactionRequest;
import app.api.response.TransactionResponse;
import app.exception.BadInputException;
import app.exception.EntityNotFoundException;
import app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;

    @PostMapping("transactions")
    public Mono<ResponseEntity<TransactionResponse>> createTransaction(@RequestBody @Valid Mono<TransactionRequest> transactionRequest) {
        return transactionRequest.map(req -> Mono.just(transactionMapper.toTransaction(req)))
                .flatMap(transactionService::createTransaction)
                .map(trans -> ResponseEntity.status(HttpStatus.OK).body(transactionMapper.toTransactionResponse(trans)))
                .onErrorResume(ex -> Mono.error(new BadInputException(ex.getMessage())));
    }

    @GetMapping("transactions")
    public Flux<TransactionResponse> getAllTransaction() {
       return transactionService.getAllTransaction()
               .map(transactionMapper::toTransactionResponse)
               .switchIfEmpty(Mono.error(new EntityNotFoundException("Transactions")));
    }
}
