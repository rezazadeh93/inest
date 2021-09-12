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
    @NotBlank(message = "{validation.required}")
    @Email(message = "{validation.email}")
    private String email;

    @NotBlank(message = "{validation.required}")
    private String password;
}
