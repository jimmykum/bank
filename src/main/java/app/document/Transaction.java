package app.document;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@Document
@ToString
public class Transaction {
    @Id
    private UUID transactionId;
    private UUID accountId;
    private BigDecimal amount;
    private String operationTypeId;
    private Instant eventDate;
}
