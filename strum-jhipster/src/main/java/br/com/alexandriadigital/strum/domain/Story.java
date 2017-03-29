package br.com.alexandriadigital.strum.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import br.com.alexandriadigital.strum.domain.enumeration.StoryOrigin;

import br.com.alexandriadigital.strum.domain.enumeration.StoryStatus;

/**
 * A Story.
 */
@Entity
@Table(name = "story")
@Document(indexName = "story")
public class Story implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "points")
    private Integer points;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "origin", nullable = false)
    private StoryOrigin origin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StoryStatus status;

    @ManyToOne(optional = false)
    @NotNull
    private User created_by;

    @ManyToOne
    private User status_by;

    @ManyToOne(optional = false)
    @NotNull
    private Sprint sprint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Story name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public Story points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public Story description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StoryOrigin getOrigin() {
        return origin;
    }

    public Story origin(StoryOrigin origin) {
        this.origin = origin;
        return this;
    }

    public void setOrigin(StoryOrigin origin) {
        this.origin = origin;
    }

    public StoryStatus getStatus() {
        return status;
    }

    public Story status(StoryStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(StoryStatus status) {
        this.status = status;
    }

    public User getCreated_by() {
        return created_by;
    }

    public Story created_by(User user) {
        this.created_by = user;
        return this;
    }

    public void setCreated_by(User user) {
        this.created_by = user;
    }

    public User getStatus_by() {
        return status_by;
    }

    public Story status_by(User user) {
        this.status_by = user;
        return this;
    }

    public void setStatus_by(User user) {
        this.status_by = user;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public Story sprint(Sprint sprint) {
        this.sprint = sprint;
        return this;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Story story = (Story) o;
        if (story.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, story.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Story{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", points='" + points + "'" +
            ", description='" + description + "'" +
            ", origin='" + origin + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
