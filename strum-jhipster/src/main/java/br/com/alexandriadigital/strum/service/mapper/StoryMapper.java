package br.com.alexandriadigital.strum.service.mapper;

import br.com.alexandriadigital.strum.domain.*;
import br.com.alexandriadigital.strum.service.dto.StoryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Story and its DTO StoryDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, SprintMapper.class, })
public interface StoryMapper {

    @Mapping(source = "created_by.id", target = "created_byId")
    @Mapping(source = "created_by.login", target = "created_byLogin")
    @Mapping(source = "status_by.id", target = "status_byId")
    @Mapping(source = "status_by.login", target = "status_byLogin")
    @Mapping(source = "sprint.id", target = "sprintId")
    @Mapping(source = "sprint.name", target = "sprintName")
    StoryDTO storyToStoryDTO(Story story);

    List<StoryDTO> storiesToStoryDTOs(List<Story> stories);

    @Mapping(source = "created_byId", target = "created_by")
    @Mapping(source = "status_byId", target = "status_by")
    @Mapping(source = "sprintId", target = "sprint")
    Story storyDTOToStory(StoryDTO storyDTO);

    List<Story> storyDTOsToStories(List<StoryDTO> storyDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Story storyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Story story = new Story();
        story.setId(id);
        return story;
    }
    

}
