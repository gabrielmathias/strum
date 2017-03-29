package br.com.alexandriadigital.strum.service;

import br.com.alexandriadigital.strum.domain.Sprint;
import br.com.alexandriadigital.strum.repository.SprintRepository;
import br.com.alexandriadigital.strum.repository.search.SprintSearchRepository;
import br.com.alexandriadigital.strum.service.dto.SprintDTO;
import br.com.alexandriadigital.strum.service.mapper.SprintMapper;
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
 * Service Implementation for managing Sprint.
 */
@Service
@Transactional
public class SprintService {

    private final Logger log = LoggerFactory.getLogger(SprintService.class);
    
    private final SprintRepository sprintRepository;

    private final SprintMapper sprintMapper;

    private final SprintSearchRepository sprintSearchRepository;

    public SprintService(SprintRepository sprintRepository, SprintMapper sprintMapper, SprintSearchRepository sprintSearchRepository) {
        this.sprintRepository = sprintRepository;
        this.sprintMapper = sprintMapper;
        this.sprintSearchRepository = sprintSearchRepository;
    }

    /**
     * Save a sprint.
     *
     * @param sprintDTO the entity to save
     * @return the persisted entity
     */
    public SprintDTO save(SprintDTO sprintDTO) {
        log.debug("Request to save Sprint : {}", sprintDTO);
        Sprint sprint = sprintMapper.sprintDTOToSprint(sprintDTO);
        sprint = sprintRepository.save(sprint);
        SprintDTO result = sprintMapper.sprintToSprintDTO(sprint);
        sprintSearchRepository.save(sprint);
        return result;
    }

    /**
     *  Get all the sprints.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SprintDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sprints");
        Page<Sprint> result = sprintRepository.findAll(pageable);
        return result.map(sprint -> sprintMapper.sprintToSprintDTO(sprint));
    }

    /**
     *  Get one sprint by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SprintDTO findOne(Long id) {
        log.debug("Request to get Sprint : {}", id);
        Sprint sprint = sprintRepository.findOne(id);
        SprintDTO sprintDTO = sprintMapper.sprintToSprintDTO(sprint);
        return sprintDTO;
    }

    /**
     *  Delete the  sprint by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Sprint : {}", id);
        sprintRepository.delete(id);
        sprintSearchRepository.delete(id);
    }

    /**
     * Search for the sprint corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SprintDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Sprints for query {}", query);
        Page<Sprint> result = sprintSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(sprint -> sprintMapper.sprintToSprintDTO(sprint));
    }
}
