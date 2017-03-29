package br.com.alexandriadigital.strum.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import br.com.alexandriadigital.strum.domain.enumeration.TaskStatus;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Document(indexName = "task")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status_message")
    private String statusMessage;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;

    @ManyToOne
    private User statusBy;

    @ManyToOne
    private User doneBy;

    @ManyToOne
    private User testedBy;

    @ManyToOne(optional = false)
    @NotNull
    private Story story;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Task name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Task description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public Task statusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Task status(TaskStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public Task createdBy(User user) {
        this.createdBy = user;
        return this;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public User getStatusBy() {
        return statusBy;
    }

    public Task statusBy(User user) {
        this.statusBy = user;
        return this;
    }

    public void setStatusBy(User user) {
        this.statusBy = user;
    }

    public User getDoneBy() {
        return doneBy;
    }

    public Task doneBy(User user) {
        this.doneBy = user;
        return this;
    }

    public void setDoneBy(User user) {
        this.doneBy = user;
    }

    public User getTestedBy() {
        return testedBy;
    }

    public Task testedBy(User user) {
        this.testedBy = user;
        return this;
    }

    public void setTestedBy(User user) {
        this.testedBy = user;
    }

    public Story getStory() {
        return story;
    }

    public Task story(Story story) {
        this.story = story;
        return this;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        if (task.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", statusMessage='" + statusMessage + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
