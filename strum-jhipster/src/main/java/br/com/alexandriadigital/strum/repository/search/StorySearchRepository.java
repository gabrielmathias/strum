package br.com.alexandriadigital.strum.repository.search;

import br.com.alexandriadigital.strum.domain.Story;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Story entity.
 */
public interface StorySearchRepository extends ElasticsearchRepository<Story, Long> {
}
