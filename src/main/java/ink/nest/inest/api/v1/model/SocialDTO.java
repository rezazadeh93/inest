package ink.nest.inest.api.v1.model;

import ink.nest.inest.annotation.SocialNameMatches;
import ink.nest.inest.validation.PostMethod;
import ink.nest.inest.validation.PutMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialDTO {
    @NotNull(groups = PutMethod.class, message = "{message.id}")
    private Long id;

     @NotNull(groups = {PostMethod.class, PutMethod.class},  message = "{message.linkID}")
    private Long linkID;

    @SocialNameMatches(groups = PostMethod.class)
    @NotBlank(groups = PostMethod.class, message = "{message.name}")
    private String name;

    @NotBlank(groups = {PostMethod.class, PutMethod.class}, message = "{message.label}")
    private String label;

    @URL(groups = {PostMethod.class, PutMethod.class}, message = "{message.urlNotValid}")
    @NotBlank(groups = {PostMethod.class, PutMethod.class}, message = "{message.url}")
    private String url;
}
