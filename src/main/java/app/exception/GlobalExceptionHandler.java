package app.exception;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        System.out.println("-----"+ex);

        if (ex instanceof EntityNotFoundException) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            DataBuffer db = new DefaultDataBufferFactory().wrap(ex.getMessage().getBytes());
           return  exchange.getResponse().writeWith(Mono.just(db));
        }
        if (ex instanceof BadInputException) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            DataBuffer db = new DefaultDataBufferFactory().wrap(ex.getMessage().getBytes());
            return  exchange.getResponse().writeWith(Mono.just(db));
        }
        return Mono.empty();
    }
}
