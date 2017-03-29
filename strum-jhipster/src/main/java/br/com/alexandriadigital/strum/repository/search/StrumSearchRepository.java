package br.com.alexandriadigital.strum.repository.search;

import br.com.alexandriadigital.strum.domain.Strum;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Strum entity.
 */
public interface StrumSearchRepository extends ElasticsearchRepository<Strum, Long> {
}
