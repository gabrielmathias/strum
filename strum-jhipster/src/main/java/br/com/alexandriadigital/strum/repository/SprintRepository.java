package br.com.alexandriadigital.strum.repository;

import br.com.alexandriadigital.strum.domain.Sprint;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sprint entity.
 */
@SuppressWarnings("unused")
public interface SprintRepository extends JpaRepository<Sprint,Long> {

    @Query("select sprint from Sprint sprint where sprint.created_by.login = ?#{principal.username}")
    List<Sprint> findByCreated_byIsCurrentUser();

}
