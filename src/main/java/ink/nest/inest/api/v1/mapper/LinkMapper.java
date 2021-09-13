package ink.nest.inest.api.v1.mapper;

import ink.nest.inest.api.v1.model.LinkDTO;
import ink.nest.inest.domain.Link;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LinkMapper {
    LinkMapper INSTANCE = Mappers.getMapper(LinkMapper.class);

    LinkDTO linkToLinkDTO(Link link);

    Link dtoLinkToLink(LinkDTO linkDTO);
}
