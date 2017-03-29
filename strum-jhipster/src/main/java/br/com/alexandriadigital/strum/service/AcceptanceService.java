package br.com.alexandriadigital.strum.service;

import br.com.alexandriadigital.strum.domain.Acceptance;
import br.com.alexandriadigital.strum.repository.AcceptanceRepository;
import br.com.alexandriadigital.strum.repository.search.AcceptanceSearchRepository;
import br.com.alexandriadigital.strum.service.dto.AcceptanceDTO;
import br.com.alexandriadigital.strum.service.mapper.AcceptanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Acceptance.
 */
@Service
@Transactional
public class AcceptanceService {

    private final Logger log = LoggerFactory.getLogger(AcceptanceService.class);
    
    private final AcceptanceRepository acceptanceRepository;

    private final AcceptanceMapper acceptanceMapper;

    private final AcceptanceSearchRepository acceptanceSearchRepository;

    public AcceptanceService(AcceptanceRepository acceptanceRepository, AcceptanceMapper acceptanceMapper, AcceptanceSearchRepository acceptanceSearchRepository) {
        this.acceptanceRepository = acceptanceRepository;
        this.acceptanceMapper = acceptanceMapper;
        this.acceptanceSearchRepository = acceptanceSearchRepository;
    }

    /**
     * Save a acceptance.
     *
     * @param acceptanceDTO the entity to save
     * @return the persisted entity
     */
    public AcceptanceDTO save(AcceptanceDTO acceptanceDTO) {
        log.debug("Request to save Acceptance : {}", acceptanceDTO);
        Acceptance acceptance = acceptanceMapper.acceptanceDTOToAcceptance(acceptanceDTO);
        acceptance = acceptanceRepository.save(acceptance);
        AcceptanceDTO result = acceptanceMapper.acceptanceToAcceptanceDTO(acceptance);
        acceptanceSearchRepository.save(acceptance);
        return result;
    }

    /**
     *  Get all the acceptances.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AcceptanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Acceptances");
        Page<Acceptance> result = acceptanceRepository.findAll(pageable);
        return result.map(acceptance -> acceptanceMapper.acceptanceToAcceptanceDTO(acceptance));
    }

    /**
     *  Get one acceptance by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AcceptanceDTO findOne(Long id) {
        log.debug("Request to get Acceptance : {}", id);
        Acceptance acceptance = acceptanceRepository.findOne(id);
        AcceptanceDTO acceptanceDTO = acceptanceMapper.acceptanceToAcceptanceDTO(acceptance);
        return acceptanceDTO;
    }

    /**
     *  Delete the  acceptance by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Acceptance : {}", id);
        acceptanceRepository.delete(id);
        acceptanceSearchRepository.delete(id);
    }

    /**
     * Search for the acceptance corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AcceptanceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Acceptances for query {}", query);
        Page<Acceptance> result = acceptanceSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(acceptance -> acceptanceMapper.acceptanceToAcceptanceDTO(acceptance));
    }
}
