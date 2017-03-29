package br.com.alexandriadigital.strum.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.alexandriadigital.strum.service.StoryService;
import br.com.alexandriadigital.strum.web.rest.util.HeaderUtil;
import br.com.alexandriadigital.strum.web.rest.util.PaginationUtil;
import br.com.alexandriadigital.strum.service.dto.StoryDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Story.
 */
@RestController
@RequestMapping("/api")
public class StoryResource {

    private final Logger log = LoggerFactory.getLogger(StoryResource.class);

    private static final String ENTITY_NAME = "story";
        
    private final StoryService storyService;

    public StoryResource(StoryService storyService) {
        this.storyService = storyService;
    }

    /**
     * POST  /stories : Create a new story.
     *
     * @param storyDTO the storyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storyDTO, or with status 400 (Bad Request) if the story has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stories")
    @Timed
    public ResponseEntity<StoryDTO> createStory(@Valid @RequestBody StoryDTO storyDTO) throws URISyntaxException {
        log.debug("REST request to save Story : {}", storyDTO);
        if (storyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new story cannot already have an ID")).body(null);
        }
        StoryDTO result = storyService.save(storyDTO);
        return ResponseEntity.created(new URI("/api/stories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stories : Updates an existing story.
     *
     * @param storyDTO the storyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storyDTO,
     * or with status 400 (Bad Request) if the storyDTO is not valid,
     * or with status 500 (Internal Server Error) if the storyDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stories")
    @Timed
    public ResponseEntity<StoryDTO> updateStory(@Valid @RequestBody StoryDTO storyDTO) throws URISyntaxException {
        log.debug("REST request to update Story : {}", storyDTO);
        if (storyDTO.getId() == null) {
            return createStory(storyDTO);
        }
        StoryDTO result = storyService.save(storyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stories : get all the stories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stories in body
     */
    @GetMapping("/stories")
    @Timed
    public ResponseEntity<List<StoryDTO>> getAllStories(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Stories");
        Page<StoryDTO> page = storyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stories/:id : get the "id" story.
     *
     * @param id the id of the storyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stories/{id}")
    @Timed
    public ResponseEntity<StoryDTO> getStory(@PathVariable Long id) {
        log.debug("REST request to get Story : {}", id);
        StoryDTO storyDTO = storyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(storyDTO));
    }

    /**
     * DELETE  /stories/:id : delete the "id" story.
     *
     * @param id the id of the storyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stories/{id}")
    @Timed
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        log.debug("REST request to delete Story : {}", id);
        storyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stories?query=:query : search for the story corresponding
     * to the query.
     *
     * @param query the query of the story search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stories")
    @Timed
    public ResponseEntity<List<StoryDTO>> searchStories(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Stories for query {}", query);
        Page<StoryDTO> page = storyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
