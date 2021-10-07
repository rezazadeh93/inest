package ink.nest.inest.service;

import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.domain.Account;
import ink.nest.inest.exception.ResourceNotFoundException;
import ink.nest.inest.security.JwtTokenUtil;
import ink.nest.inest.utility.Messages;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Objects;

@Service
public class ActiveUserService {
    private final AccountCrudService accountCrudService;
    private final HttpServletRequest request;
    private final JwtTokenUtil jwtTokenUtil;
    private final Messages messages;

    public ActiveUserService(AccountCrudService accountCrudService, HttpServletRequest request, JwtTokenUtil jwtTokenUtil, Messages messages) {
        this.accountCrudService = accountCrudService;
        this.request = request;
        this.jwtTokenUtil = jwtTokenUtil;
        this.messages = messages;
    }

    Account currentAccount() {
        //get token from header
        String token = Objects.requireNonNull(request.getHeader(InestApiConstant.HEADER_AUTHORIZATION))
                .replace(InestApiConstant.HEADER_BEARER, "");

        String username = jwtTokenUtil.getUsername(token);

        return accountCrudService.findAccountByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                                messages.getExceptionMessage(
                                        "message.notFound",
                                        Collections.singletonList(username)
                                )
                        )
                );

    }
}
