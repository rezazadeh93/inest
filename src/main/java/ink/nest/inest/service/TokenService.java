package ink.nest.inest.service;

import ink.nest.inest.api.v1.model.TokenDTO;

public interface TokenService {
    TokenDTO createToken();

    TokenDTO refreshToken(String refreshToken);
}
