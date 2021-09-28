package ink.nest.inest.api.v1.model;

import ink.nest.inest.annotation.SocialNameMatches;
import ink.nest.inest.validation.SocialPost;
import ink.nest.inest.validation.SocialPut;
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
    @NotNull(groups = SocialPut.class, message = "{message.id}")
    private Long id;

     @NotNull(groups = {SocialPost.class, SocialPut.class},  message = "{message.linkID}")
    private Long linkID;

    @SocialNameMatches(groups = SocialPost.class)
    @NotBlank(groups = SocialPost.class, message = "{message.name}")
    private String name;

    @NotBlank(groups = {SocialPost.class, SocialPut.class}, message = "{message.label}")
    private String label;

    @URL(groups = {SocialPost.class, SocialPut.class}, message = "{message.urlNotValid}")
    @NotBlank(groups = {SocialPost.class, SocialPut.class}, message = "{message.url}")
    private String url;
}
