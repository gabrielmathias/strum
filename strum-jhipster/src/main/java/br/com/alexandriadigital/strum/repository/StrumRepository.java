package br.com.alexandriadigital.strum.repository;

import br.com.alexandriadigital.strum.domain.Strum;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Strum entity.
 */
@SuppressWarnings("unused")
public interface StrumRepository extends JpaRepository<Strum,Long> {

    @Query("select strum from Strum strum where strum.owner.login = ?#{principal.username}")
    List<Strum> findByOwnerIsCurrentUser();

}
