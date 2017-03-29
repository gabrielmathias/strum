package br.com.alexandriadigital.strum.repository.search;

import br.com.alexandriadigital.strum.domain.Sprint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Sprint entity.
 */
public interface SprintSearchRepository extends ElasticsearchRepository<Sprint, Long> {
}
