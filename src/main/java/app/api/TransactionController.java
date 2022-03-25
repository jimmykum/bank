package app.api;

import app.api.mapper.TransactionMapper;
import app.api.request.TransactionRequest;
import app.api.response.TransactionResponse;
import app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
                .map(trans -> ResponseEntity.status(HttpStatus.OK).body(transactionMapper.toTransactionResponse(trans)));
    }
}
