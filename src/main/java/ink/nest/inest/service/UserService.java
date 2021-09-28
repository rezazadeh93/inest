package ink.nest.inest.service;

import ink.nest.inest.domain.Account;
import ink.nest.inest.exception.InternalServerException;
import ink.nest.inest.utility.Messages;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private final AccountCrudService accountCrudService;
    private final Messages messages;

    public UserService(AccountCrudService accountCrudService, Messages messages) {
        this.accountCrudService = accountCrudService;
        this.messages = messages;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        try {
            Account account = accountCrudService.findAccountByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            messages.getExceptionMessage("message.notFoundEmail", Collections.singletonList(email))
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
            throw new InternalServerException(
                    messages.getExceptionMessage("message.internalServerError", Collections.singletonList(e.getMessage()))
            );
        }
    }
}
