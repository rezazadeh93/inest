package ink.nest.inest.api.v1.mapper;

import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.api.v1.request.RegisterRequest;
import ink.nest.inest.domain.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDTO accountToAccountDTO(Account account);

    Account dtoAccountToAccount(AccountDTO account);

    @Mapping(source = "password", target = "passwordEncrypted", qualifiedByName = "passwordEncoder")
    Account dtoReqRegisterToAccount(RegisterRequest accountDTO);


    @Named("passwordEncoder")
    public static String encryptPassword(String originPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.encode(originPassword);
    }
}
