package ink.nest.inest.service;

import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.api.v1.model.TokenDTO;
import org.springframework.security.core.userdetails.User;

public interface UserAuthenticationService {
    TokenDTO login(String username, String password);

    AccountDTO findByToken(String token);

    void logout(User user);
}
