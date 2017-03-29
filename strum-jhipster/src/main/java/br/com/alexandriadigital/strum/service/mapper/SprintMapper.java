package br.com.alexandriadigital.strum.service.mapper;

import br.com.alexandriadigital.strum.domain.*;
import br.com.alexandriadigital.strum.service.dto.SprintDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Sprint and its DTO SprintDTO.
 */
@Mapper(componentModel = "spring", uses = {StrumMapper.class, UserMapper.class, })
public interface SprintMapper {

    @Mapping(source = "strum.id", target = "strumId")
    @Mapping(source = "strum.name", target = "strumName")
    @Mapping(source = "created_by.id", target = "created_byId")
    @Mapping(source = "created_by.login", target = "created_byLogin")
    SprintDTO sprintToSprintDTO(Sprint sprint);

    List<SprintDTO> sprintsToSprintDTOs(List<Sprint> sprints);

    @Mapping(source = "strumId", target = "strum")
    @Mapping(source = "created_byId", target = "created_by")
    Sprint sprintDTOToSprint(SprintDTO sprintDTO);

    List<Sprint> sprintDTOsToSprints(List<SprintDTO> sprintDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Sprint sprintFromId(Long id) {
        if (id == null) {
            return null;
        }
        Sprint sprint = new Sprint();
        sprint.setId(id);
        return sprint;
    }
    

}
