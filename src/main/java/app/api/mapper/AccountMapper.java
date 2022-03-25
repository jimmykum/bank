package app.api.mapper;

import app.api.request.AccountRequest;
import app.api.response.AccountResponse;
import app.document.Account;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {

    Account toAccount(AccountRequest accountRequest);
    AccountResponse toAccountResponse(Account account);
}
