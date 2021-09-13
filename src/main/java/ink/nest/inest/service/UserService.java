package ink.nest.inest.service;

import ink.nest.inest.domain.Account;
import ink.nest.inest.exception.ExceptionMessages;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private final AccountCrudService accountCrudService;

    public UserService(AccountCrudService accountCrudService) {
        this.accountCrudService = accountCrudService;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        try {
            Account account = accountCrudService.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            ExceptionMessages.getNotFoundException(email)
                    ));

            Collection<GrantedAuthority> authorities = Collections.singleton(
                    new SimpleGrantedAuthority(account.getPermission().name())
            );

            return new User(
                    account.getEmail(),
                    account.getPasswordEncrypted(),
                    account.isVerified(),
                    true,
                    true,
                    true,
                    authorities
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionMessages.getInternalSeverException(e.getMessage())
            );
        }
    }
}
