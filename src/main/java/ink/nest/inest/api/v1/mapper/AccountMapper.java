package ink.nest.inest.api.v1.mapper;

import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.api.v1.model.ReqRegisterDTO;
import ink.nest.inest.domain.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDTO accountToAccountDTO(Account account);

    Account ReqRegisterDTOToAccount(ReqRegisterDTO accountDTO);
}
