package app.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
@ToString
public class TransactionResponse {
    @JsonProperty("transaction_id")
    private UUID transactionId;
    @JsonProperty("account_id")
    private UUID accountId;
    @JsonProperty("operation_type_id")
    private String operationTypeId;
    private BigDecimal amount;
    private BigDecimal balance;
    private Instant eventDate;
}
