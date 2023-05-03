package com.evolunteer.evm.common.mapper;

import com.evolunteer.evm.common.domain.dto.AccountDto;
import com.evolunteer.evm.common.domain.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    public abstract Account mapAccountDtoToAccount(AccountDto accountDto);

    public abstract AccountDto mapAccountToAccountDto(Account account);
}
