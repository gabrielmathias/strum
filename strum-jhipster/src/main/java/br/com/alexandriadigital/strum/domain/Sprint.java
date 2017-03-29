package br.com.alexandriadigital.strum.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import br.com.alexandriadigital.strum.domain.enumeration.SprintStatus;

/**
 * A Sprint.
 */
@Entity
@Table(name = "sprint")
@Document(indexName = "sprint")
public class Sprint implements Serializable {

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

    @Column(name = "start_at")
    private ZonedDateTime start_at;

    @Column(name = "end_at")
    private ZonedDateTime end_at;

    @Column(name = "days_planned")
    private Integer days_planned;

    @Column(name = "days_used")
    private Integer days_used;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SprintStatus status;

    @ManyToOne(optional = false)
    @NotNull
    private Strum strum;

    @ManyToOne(optional = false)
    @NotNull
    private User created_by;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Sprint name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Sprint description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getStart_at() {
        return start_at;
    }

    public Sprint start_at(ZonedDateTime start_at) {
        this.start_at = start_at;
        return this;
    }

    public void setStart_at(ZonedDateTime start_at) {
        this.start_at = start_at;
    }

    public ZonedDateTime getEnd_at() {
        return end_at;
    }

    public Sprint end_at(ZonedDateTime end_at) {
        this.end_at = end_at;
        return this;
    }

    public void setEnd_at(ZonedDateTime end_at) {
        this.end_at = end_at;
    }

    public Integer getDays_planned() {
        return days_planned;
    }

    public Sprint days_planned(Integer days_planned) {
        this.days_planned = days_planned;
        return this;
    }

    public void setDays_planned(Integer days_planned) {
        this.days_planned = days_planned;
    }

    public Integer getDays_used() {
        return days_used;
    }

    public Sprint days_used(Integer days_used) {
        this.days_used = days_used;
        return this;
    }

    public void setDays_used(Integer days_used) {
        this.days_used = days_used;
    }

    public SprintStatus getStatus() {
        return status;
    }

    public Sprint status(SprintStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(SprintStatus status) {
        this.status = status;
    }

    public Strum getStrum() {
        return strum;
    }

    public Sprint strum(Strum strum) {
        this.strum = strum;
        return this;
    }

    public void setStrum(Strum strum) {
        this.strum = strum;
    }

    public User getCreated_by() {
        return created_by;
    }

    public Sprint created_by(User user) {
        this.created_by = user;
        return this;
    }

    public void setCreated_by(User user) {
        this.created_by = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sprint sprint = (Sprint) o;
        if (sprint.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sprint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sprint{" +
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
