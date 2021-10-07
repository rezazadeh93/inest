package ink.nest.inest.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ink.nest.inest.api.v1.model.OtherLinkDTO;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class HashSetConverter implements AttributeConverter<Set<OtherLinkDTO>, String> {
    private final ObjectMapper objectMapper;

    public HashSetConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(Set<OtherLinkDTO> otherLinks) {
        String otherLinkJSON = null;
        try {
            otherLinkJSON = objectMapper.writeValueAsString(otherLinks);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }

        return otherLinkJSON;
    }

    @Override
    public Set<OtherLinkDTO> convertToEntityAttribute(String otherLinkJSON) {
        Set<OtherLinkDTO> otherLink = null;

        if (Objects.isNull(otherLinkJSON))
            return new HashSet<>();

        try {
            otherLink = objectMapper.readValue(otherLinkJSON, new TypeReference<>() {});
        } catch (final IOException e) {
            log.error("JSON reading error", e);
        }

        return otherLink;
    }
}
