package ink.nest.inest.api.v1.mapper;

import ink.nest.inest.api.v1.model.SocialDTO;
import ink.nest.inest.domain.Social;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SocialMapper {
    SocialMapper INSTANCE = Mappers.getMapper(SocialMapper.class);

    SocialDTO socialToSocialDTO(Social social);

    Social dotSocialTOSocial(SocialDTO socialDTO);
}
