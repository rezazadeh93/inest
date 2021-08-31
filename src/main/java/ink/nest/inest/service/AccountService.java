package ink.nest.inest.service;

import ink.nest.inest.api.v1.model.AccountDTO;

import java.util.Set;

public interface AccountService {
    Set<AccountDTO> getAll();

    AccountDTO findByID(Long id);

    AccountDTO saveAccountDTO(AccountDTO accountDTO);

    void softDeleteByIdAccount(Long id);
}
