package ink.nest.inest.api.v1.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "{message.email}")
    @Email(message = "{message.badEmail}")
    private String email;

    @NotBlank(message = "{message.password}")
    private String password;
}
