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
import javax.validation.constraints.Size;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class AccountRequest {
    @JsonProperty("account_id")
    private UUID accountId;
    @JsonProperty("document_number")
    @NotNull
    @Size(message = "document number must be between 5 and 15 characters", min = 5, max = 15)
    private String documentNumber;
}
