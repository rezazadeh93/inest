package ink.nest.inest.service;

import com.sun.jdi.InternalException;
import ink.nest.inest.api.v1.model.TokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {
    private static final String uri = "{baseUrl}/realms/{realmsName}/protocol/openid-connect/token";
    @Value("${keycloak.url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.username}")
    private String keycloakUsername;

    @Value("${keycloak.password}")
    private String keycloakPassword;

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public TokenDTO createToken() {
        String data = UriComponentsBuilder
                .fromUriString("")
                .queryParam("client_id", keycloakClientId)
                .queryParam("grant_type", "password")
                .queryParam("username", keycloakUsername)
                .queryParam("password", keycloakPassword)
                .toUriString()
                .replace("?", "");

        return sendRequest(data);
    }

    @Override
    public TokenDTO refreshToken(String refreshToken) {
        String data = UriComponentsBuilder
                .fromUriString("")
                .queryParam("client_id", keycloakClientId)
                .queryParam("refresh_token", refreshToken)
                .toUriString()
                .replace("?", "");

        return sendRequest(data);
    }

    private TokenDTO sendRequest(String data) {
        try {
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put("baseUrl", keycloakUrl);
            uriParams.put("realmsName", keycloakRealm);

            // Query parameters
            UriComponents uriBuilt = UriComponentsBuilder.fromUriString(uri).buildAndExpand(uriParams);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<String> entity = new HttpEntity<>(data, headers);
            ResponseEntity<TokenDTO> response = restTemplate.exchange(uriBuilt.toUri(),
                    HttpMethod.POST,
                    entity,
                    TokenDTO.class);

            if (response.getStatusCode().value() != HttpStatus.OK.value()) {
                log.error("Unauthorised access to protected resource, status code: " + response.getStatusCode());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return response.getBody();

        } catch (Exception ex) {
            log.error("Unauthorised access to protected resource", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
