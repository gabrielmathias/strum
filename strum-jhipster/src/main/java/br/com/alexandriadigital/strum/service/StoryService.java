package br.com.alexandriadigital.strum.service;

import br.com.alexandriadigital.strum.domain.Story;
import br.com.alexandriadigital.strum.repository.StoryRepository;
import br.com.alexandriadigital.strum.repository.search.StorySearchRepository;
import br.com.alexandriadigital.strum.service.dto.StoryDTO;
import br.com.alexandriadigital.strum.service.mapper.StoryMapper;
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
 * Service Implementation for managing Story.
 */
@Service
@Transactional
public class StoryService {

    private final Logger log = LoggerFactory.getLogger(StoryService.class);
    
    private final StoryRepository storyRepository;

    private final StoryMapper storyMapper;

    private final StorySearchRepository storySearchRepository;

    public StoryService(StoryRepository storyRepository, StoryMapper storyMapper, StorySearchRepository storySearchRepository) {
        this.storyRepository = storyRepository;
        this.storyMapper = storyMapper;
        this.storySearchRepository = storySearchRepository;
    }

    /**
     * Save a story.
     *
     * @param storyDTO the entity to save
     * @return the persisted entity
     */
    public StoryDTO save(StoryDTO storyDTO) {
        log.debug("Request to save Story : {}", storyDTO);
        Story story = storyMapper.storyDTOToStory(storyDTO);
        story = storyRepository.save(story);
        StoryDTO result = storyMapper.storyToStoryDTO(story);
        storySearchRepository.save(story);
        return result;
    }

    /**
     *  Get all the stories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stories");
        Page<Story> result = storyRepository.findAll(pageable);
        return result.map(story -> storyMapper.storyToStoryDTO(story));
    }

    /**
     *  Get one story by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public StoryDTO findOne(Long id) {
        log.debug("Request to get Story : {}", id);
        Story story = storyRepository.findOne(id);
        StoryDTO storyDTO = storyMapper.storyToStoryDTO(story);
        return storyDTO;
    }

    /**
     *  Delete the  story by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Story : {}", id);
        storyRepository.delete(id);
        storySearchRepository.delete(id);
    }

    /**
     * Search for the story corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Stories for query {}", query);
        Page<Story> result = storySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(story -> storyMapper.storyToStoryDTO(story));
    }
}
