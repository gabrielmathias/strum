package br.com.alexandriadigital.strum.repository;

import br.com.alexandriadigital.strum.domain.Acceptance;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Acceptance entity.
 */
@SuppressWarnings("unused")
public interface AcceptanceRepository extends JpaRepository<Acceptance,Long> {

    @Query("select acceptance from Acceptance acceptance where acceptance.created_by.login = ?#{principal.username}")
    List<Acceptance> findByCreated_byIsCurrentUser();

    @Query("select acceptance from Acceptance acceptance where acceptance.status_by.login = ?#{principal.username}")
    List<Acceptance> findByStatus_byIsCurrentUser();

}
