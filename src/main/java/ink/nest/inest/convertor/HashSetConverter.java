package ink.nest.inest.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ink.nest.inest.domain.OtherLink;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Set;

@Slf4j
public class HashSetConverter implements AttributeConverter<Set<OtherLink>, String> {
    private final ObjectMapper objectMapper;

    public HashSetConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(Set<OtherLink> otherLinks) {
        String otherLinkJSON = null;
        try {
            otherLinkJSON = objectMapper.writeValueAsString(otherLinks);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }

        return otherLinkJSON;
    }

    @Override
    public Set<OtherLink> convertToEntityAttribute(String otherLinkJSON) {
        Set<OtherLink> otherLink = null;
        try {
            otherLink = objectMapper.readValue(otherLinkJSON, Set.class);
        } catch (final IOException e) {
            log.error("JSON reading error", e);
        }

        return otherLink;
    }
}
