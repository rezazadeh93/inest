package ink.nest.inest.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqRegisterDTO implements Serializable {
    @NotBlank(message = "{validation.required}")
    @Email(message = "{validation.email}")
    private String email;

    @NotBlank(message = "{validation.required}")
    private String password;

    @NotBlank(message = "{validation.required}")
    private String confirmPassword;
}
