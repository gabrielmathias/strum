package br.com.alexandriadigital.strum.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import br.com.alexandriadigital.strum.domain.enumeration.AcceptanceStatus;

/**
 * A Acceptance.
 */
@Entity
@Table(name = "acceptance")
@Document(indexName = "acceptance")
public class Acceptance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "criteria", nullable = false)
    private String criteria;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AcceptanceStatus status;

    @Column(name = "message")
    private String message;

    @ManyToOne(optional = false)
    @NotNull
    private User created_by;

    @ManyToOne
    private User status_by;

    @ManyToOne(optional = false)
    @NotNull
    private Story story;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCriteria() {
        return criteria;
    }

    public Acceptance criteria(String criteria) {
        this.criteria = criteria;
        return this;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public AcceptanceStatus getStatus() {
        return status;
    }

    public Acceptance status(AcceptanceStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(AcceptanceStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public Acceptance message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getCreated_by() {
        return created_by;
    }

    public Acceptance created_by(User user) {
        this.created_by = user;
        return this;
    }

    public void setCreated_by(User user) {
        this.created_by = user;
    }

    public User getStatus_by() {
        return status_by;
    }

    public Acceptance status_by(User user) {
        this.status_by = user;
        return this;
    }

    public void setStatus_by(User user) {
        this.status_by = user;
    }

    public Story getStory() {
        return story;
    }

    public Acceptance story(Story story) {
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
        Acceptance acceptance = (Acceptance) o;
        if (acceptance.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, acceptance.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Acceptance{" +
            "id=" + id +
            ", criteria='" + criteria + "'" +
            ", status='" + status + "'" +
            ", message='" + message + "'" +
            '}';
    }
}
