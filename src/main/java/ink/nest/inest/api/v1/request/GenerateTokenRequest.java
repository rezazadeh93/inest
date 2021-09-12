package ink.nest.inest.api.v1.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateTokenRequest {
    private String username = "";
    private Collection<GrantedAuthority> authorities = List.of();
}
