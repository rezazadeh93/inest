package ink.nest.inest.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDTO {
    private Long id;

    private String name;
    private String pic;

    private Integer price;
    private Long view;
    private Long download;
}
