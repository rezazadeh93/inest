package ink.nest.inest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherLink implements Serializable {
    private String name;
    private String label;
    private String url;
}
