package ink.nest.inest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.api.v1.model.ReqRegisterDTO;

import javax.xml.bind.ValidationException;
import java.util.Optional;
import java.util.Set;

public interface AccountService {
    Set<AccountDTO> getAll();

    Optional<AccountDTO> findByID(Long id);

    Optional<AccountDTO> findByEmail(String email);

    AccountDTO saveAccountDTO(ReqRegisterDTO accountDTO) throws ValidationException, JsonProcessingException;

    void softDeleteByIdAccount(Long id);
}
