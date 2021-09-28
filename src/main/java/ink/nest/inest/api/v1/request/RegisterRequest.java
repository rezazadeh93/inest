package ink.nest.inest.api.v1.request;

import ink.nest.inest.annotation.PasswordMatches;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

@PasswordMatches
public class RegisterRequest implements Serializable {
    @NotBlank(message = "{message.email}")
    @Email(message = "{message.badEmail}")
    private String email;

    @NotBlank(message = "{message.password}")
    private String password;

    @NotBlank(message = "{message.confirmPassword}")
    private String confirmPassword;
}
