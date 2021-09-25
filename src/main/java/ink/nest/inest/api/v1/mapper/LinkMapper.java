package ink.nest.inest.api.v1.mapper;

import ink.nest.inest.api.v1.model.LinkDTO;
import ink.nest.inest.domain.Account;
import ink.nest.inest.domain.Link;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LinkMapper {
    LinkMapper INSTANCE = Mappers.getMapper(LinkMapper.class);

    @Mapping(source = "account", target = "accountID", qualifiedByName = "getAccountID")
    LinkDTO linkToLinkDTO(Link link);

    Link dtoLinkToLink(LinkDTO linkDTO);

    @Named("getAccountID")
    static Long getAccountID(Account account) {
        return account.getId();
    }
}
