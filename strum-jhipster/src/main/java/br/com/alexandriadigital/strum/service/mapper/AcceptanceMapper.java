package br.com.alexandriadigital.strum.service.mapper;

import br.com.alexandriadigital.strum.domain.*;
import br.com.alexandriadigital.strum.service.dto.AcceptanceDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Acceptance and its DTO AcceptanceDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, StoryMapper.class, })
public interface AcceptanceMapper {

    @Mapping(source = "created_by.id", target = "created_byId")
    @Mapping(source = "created_by.login", target = "created_byLogin")
    @Mapping(source = "status_by.id", target = "status_byId")
    @Mapping(source = "status_by.login", target = "status_byLogin")
    @Mapping(source = "story.id", target = "storyId")
    @Mapping(source = "story.name", target = "storyName")
    AcceptanceDTO acceptanceToAcceptanceDTO(Acceptance acceptance);

    List<AcceptanceDTO> acceptancesToAcceptanceDTOs(List<Acceptance> acceptances);

    @Mapping(source = "created_byId", target = "created_by")
    @Mapping(source = "status_byId", target = "status_by")
    @Mapping(source = "storyId", target = "story")
    Acceptance acceptanceDTOToAcceptance(AcceptanceDTO acceptanceDTO);

    List<Acceptance> acceptanceDTOsToAcceptances(List<AcceptanceDTO> acceptanceDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Acceptance acceptanceFromId(Long id) {
        if (id == null) {
            return null;
        }
        Acceptance acceptance = new Acceptance();
        acceptance.setId(id);
        return acceptance;
    }
    

}
