package br.com.alexandriadigital.strum.repository;

import br.com.alexandriadigital.strum.domain.Task;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("select task from Task task where task.createdBy.login = ?#{principal.username}")
    List<Task> findByCreatedByIsCurrentUser();

    @Query("select task from Task task where task.statusBy.login = ?#{principal.username}")
    List<Task> findByStatusByIsCurrentUser();

    @Query("select task from Task task where task.doneBy.login = ?#{principal.username}")
    List<Task> findByDoneByIsCurrentUser();

    @Query("select task from Task task where task.testedBy.login = ?#{principal.username}")
    List<Task> findByTestedByIsCurrentUser();

}
