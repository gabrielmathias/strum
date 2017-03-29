package br.com.alexandriadigital.strum.service;

import br.com.alexandriadigital.strum.domain.Strum;
import br.com.alexandriadigital.strum.repository.StrumRepository;
import br.com.alexandriadigital.strum.repository.search.StrumSearchRepository;
import br.com.alexandriadigital.strum.service.dto.StrumDTO;
import br.com.alexandriadigital.strum.service.mapper.StrumMapper;
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
 * Service Implementation for managing Strum.
 */
@Service
@Transactional
public class StrumService {

    private final Logger log = LoggerFactory.getLogger(StrumService.class);
    
    private final StrumRepository strumRepository;

    private final StrumMapper strumMapper;

    private final StrumSearchRepository strumSearchRepository;

    public StrumService(StrumRepository strumRepository, StrumMapper strumMapper, StrumSearchRepository strumSearchRepository) {
        this.strumRepository = strumRepository;
        this.strumMapper = strumMapper;
        this.strumSearchRepository = strumSearchRepository;
    }

    /**
     * Save a strum.
     *
     * @param strumDTO the entity to save
     * @return the persisted entity
     */
    public StrumDTO save(StrumDTO strumDTO) {
        log.debug("Request to save Strum : {}", strumDTO);
        Strum strum = strumMapper.strumDTOToStrum(strumDTO);
        strum = strumRepository.save(strum);
        StrumDTO result = strumMapper.strumToStrumDTO(strum);
        strumSearchRepository.save(strum);
        return result;
    }

    /**
     *  Get all the strums.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StrumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Strums");
        Page<Strum> result = strumRepository.findAll(pageable);
        return result.map(strum -> strumMapper.strumToStrumDTO(strum));
    }

    /**
     *  Get one strum by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public StrumDTO findOne(Long id) {
        log.debug("Request to get Strum : {}", id);
        Strum strum = strumRepository.findOne(id);
        StrumDTO strumDTO = strumMapper.strumToStrumDTO(strum);
        return strumDTO;
    }

    /**
     *  Delete the  strum by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Strum : {}", id);
        strumRepository.delete(id);
        strumSearchRepository.delete(id);
    }

    /**
     * Search for the strum corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StrumDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Strums for query {}", query);
        Page<Strum> result = strumSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(strum -> strumMapper.strumToStrumDTO(strum));
    }
}
