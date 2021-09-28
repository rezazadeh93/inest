package ink.nest.inest.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse {
    private String timestamp;
    private Integer status;
    private String error;
    private String message;
}
