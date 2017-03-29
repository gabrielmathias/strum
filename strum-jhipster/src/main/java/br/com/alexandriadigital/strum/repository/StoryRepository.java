package br.com.alexandriadigital.strum.repository;

import br.com.alexandriadigital.strum.domain.Story;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Story entity.
 */
@SuppressWarnings("unused")
public interface StoryRepository extends JpaRepository<Story,Long> {

    @Query("select story from Story story where story.created_by.login = ?#{principal.username}")
    List<Story> findByCreated_byIsCurrentUser();

    @Query("select story from Story story where story.status_by.login = ?#{principal.username}")
    List<Story> findByStatus_byIsCurrentUser();

}
