package br.com.alexandriadigital.strum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import br.com.alexandriadigital.strum.domain.enumeration.TaskStatus;

/**
 * A DTO for the Task entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private String statusMessage;

    @NotNull
    private TaskStatus status;

    private Long createdById;

    private String createdByLogin;

    private Long statusById;

    private String statusByLogin;

    private Long doneById;

    private String doneByLogin;

    private Long testedById;

    private String testedByLogin;

    private Long storyId;

    private String storyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    public Long getStatusById() {
        return statusById;
    }

    public void setStatusById(Long userId) {
        this.statusById = userId;
    }

    public String getStatusByLogin() {
        return statusByLogin;
    }

    public void setStatusByLogin(String userLogin) {
        this.statusByLogin = userLogin;
    }

    public Long getDoneById() {
        return doneById;
    }

    public void setDoneById(Long userId) {
        this.doneById = userId;
    }

    public String getDoneByLogin() {
        return doneByLogin;
    }

    public void setDoneByLogin(String userLogin) {
        this.doneByLogin = userLogin;
    }

    public Long getTestedById() {
        return testedById;
    }

    public void setTestedById(Long userId) {
        this.testedById = userId;
    }

    public String getTestedByLogin() {
        return testedByLogin;
    }

    public void setTestedByLogin(String userLogin) {
        this.testedByLogin = userLogin;
    }

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;

        if ( ! Objects.equals(id, taskDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", statusMessage='" + statusMessage + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
