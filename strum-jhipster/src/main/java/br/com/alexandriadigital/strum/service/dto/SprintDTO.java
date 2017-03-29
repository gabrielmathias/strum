package br.com.alexandriadigital.strum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import br.com.alexandriadigital.strum.domain.enumeration.SprintStatus;

/**
 * A DTO for the Sprint entity.
 */
public class SprintDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private ZonedDateTime start_at;

    private ZonedDateTime end_at;

    private Integer days_planned;

    private Integer days_used;

    @NotNull
    private SprintStatus status;

    private Long strumId;

    private String strumName;

    private Long created_byId;

    private String created_byLogin;

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
    public ZonedDateTime getStart_at() {
        return start_at;
    }

    public void setStart_at(ZonedDateTime start_at) {
        this.start_at = start_at;
    }
    public ZonedDateTime getEnd_at() {
        return end_at;
    }

    public void setEnd_at(ZonedDateTime end_at) {
        this.end_at = end_at;
    }
    public Integer getDays_planned() {
        return days_planned;
    }

    public void setDays_planned(Integer days_planned) {
        this.days_planned = days_planned;
    }
    public Integer getDays_used() {
        return days_used;
    }

    public void setDays_used(Integer days_used) {
        this.days_used = days_used;
    }
    public SprintStatus getStatus() {
        return status;
    }

    public void setStatus(SprintStatus status) {
        this.status = status;
    }

    public Long getStrumId() {
        return strumId;
    }

    public void setStrumId(Long strumId) {
        this.strumId = strumId;
    }

    public String getStrumName() {
        return strumName;
    }

    public void setStrumName(String strumName) {
        this.strumName = strumName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SprintDTO sprintDTO = (SprintDTO) o;

        if ( ! Objects.equals(id, sprintDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SprintDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", start_at='" + start_at + "'" +
            ", end_at='" + end_at + "'" +
            ", days_planned='" + days_planned + "'" +
            ", days_used='" + days_used + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
