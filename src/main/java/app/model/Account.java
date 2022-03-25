package app.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@Document
@ToString
public class Account {
    @Id
    private UUID accountId;
    private String documentNumber;
}
