package br.com.alexandriadigital.strum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import br.com.alexandriadigital.strum.domain.enumeration.AcceptanceStatus;

/**
 * A DTO for the Acceptance entity.
 */
public class AcceptanceDTO implements Serializable {

    private Long id;

    @NotNull
    private String criteria;

    @NotNull
    private AcceptanceStatus status;

    private String message;

    private Long created_byId;

    private String created_byLogin;

    private Long status_byId;

    private String status_byLogin;

    private Long storyId;

    private String storyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }
    public AcceptanceStatus getStatus() {
        return status;
    }

    public void setStatus(AcceptanceStatus status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

        AcceptanceDTO acceptanceDTO = (AcceptanceDTO) o;

        if ( ! Objects.equals(id, acceptanceDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AcceptanceDTO{" +
            "id=" + id +
            ", criteria='" + criteria + "'" +
            ", status='" + status + "'" +
            ", message='" + message + "'" +
            '}';
    }
}
