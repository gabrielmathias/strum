package br.com.alexandriadigital.strum.repository.search;

import br.com.alexandriadigital.strum.domain.Acceptance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Acceptance entity.
 */
public interface AcceptanceSearchRepository extends ElasticsearchRepository<Acceptance, Long> {
}
