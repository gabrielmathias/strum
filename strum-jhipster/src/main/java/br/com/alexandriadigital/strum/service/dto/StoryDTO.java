package br.com.alexandriadigital.strum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import br.com.alexandriadigital.strum.domain.enumeration.StoryOrigin;
import br.com.alexandriadigital.strum.domain.enumeration.StoryStatus;

/**
 * A DTO for the Story entity.
 */
public class StoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer points;

    private String description;

    @NotNull
    private StoryOrigin origin;

    @NotNull
    private StoryStatus status;

    private Long created_byId;

    private String created_byLogin;

    private Long status_byId;

    private String status_byLogin;

    private Long sprintId;

    private String sprintName;

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
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public StoryOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(StoryOrigin origin) {
        this.origin = origin;
    }
    public StoryStatus getStatus() {
        return status;
    }

    public void setStatus(StoryStatus status) {
        this.status = status;
    }

    public Long getCreated_byId() {
        return created_byId;
    }

    public void setCreated_byId(Long userId) {
        this.created_byId = userId;
    }

    public String getCreated_byLogin() {
        return created_byLogin;
    }

    public void setCreated_byLogin(String userLogin) {
        this.created_byLogin = userLogin;
    }

    public Long getStatus_byId() {
        return status_byId;
    }

    public void setStatus_byId(Long userId) {
        this.status_byId = userId;
    }

    public String getStatus_byLogin() {
        return status_byLogin;
    }

    public void setStatus_byLogin(String userLogin) {
        this.status_byLogin = userLogin;
    }

    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    public String getSprintName() {
        return sprintName;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StoryDTO storyDTO = (StoryDTO) o;

        if ( ! Objects.equals(id, storyDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StoryDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", points='" + points + "'" +
            ", description='" + description + "'" +
            ", origin='" + origin + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
