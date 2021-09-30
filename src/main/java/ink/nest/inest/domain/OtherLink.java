package ink.nest.inest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherLink implements Serializable {
    @NotBlank(message = "{message.name}")
    private String name;

    @NotBlank(message = "{message.label}")
    private String label;

    @URL(message = "{message.urlNotValid}")
    @NotBlank(message = "{message.url}")
    private String url;
}
