package app.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class TransactionRequest {
    @NotNull
    @JsonProperty("account_id")
    private UUID accountId;
    @NotNull
    @JsonProperty("operation_type_id")
    private String operationTypeId;
    @NotNull
    private BigDecimal amount;
}
