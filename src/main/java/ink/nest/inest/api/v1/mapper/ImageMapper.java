package ink.nest.inest.api.v1.mapper;

import ink.nest.inest.api.v1.model.ImageFileDTO;
import ink.nest.inest.api.v1.model.LinkDTO;
import ink.nest.inest.domain.Account;
import ink.nest.inest.domain.Link;
import ink.nest.inest.domain.mongodb.ImageFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);

    ImageFileDTO imageToImageDTO(ImageFile imageFile);

    ImageFile dtoImageToImage(ImageFileDTO imageFileDTO);
}
