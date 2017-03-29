package br.com.alexandriadigital.strum.service.mapper;

import br.com.alexandriadigital.strum.domain.*;
import br.com.alexandriadigital.strum.service.dto.TaskDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Task and its DTO TaskDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, StoryMapper.class, })
public interface TaskMapper {

    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "statusBy.id", target = "statusById")
    @Mapping(source = "statusBy.login", target = "statusByLogin")
    @Mapping(source = "doneBy.id", target = "doneById")
    @Mapping(source = "doneBy.login", target = "doneByLogin")
    @Mapping(source = "testedBy.id", target = "testedById")
    @Mapping(source = "testedBy.login", target = "testedByLogin")
    @Mapping(source = "story.id", target = "storyId")
    @Mapping(source = "story.name", target = "storyName")
    TaskDTO taskToTaskDTO(Task task);

    List<TaskDTO> tasksToTaskDTOs(List<Task> tasks);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "statusById", target = "statusBy")
    @Mapping(source = "doneById", target = "doneBy")
    @Mapping(source = "testedById", target = "testedBy")
    @Mapping(source = "storyId", target = "story")
    Task taskDTOToTask(TaskDTO taskDTO);

    List<Task> taskDTOsToTasks(List<TaskDTO> taskDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Task taskFromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }
    

}
