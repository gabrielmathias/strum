package br.com.alexandriadigital.strum.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.alexandriadigital.strum.service.SprintService;
import br.com.alexandriadigital.strum.web.rest.util.HeaderUtil;
import br.com.alexandriadigital.strum.web.rest.util.PaginationUtil;
import br.com.alexandriadigital.strum.service.dto.SprintDTO;
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
 * REST controller for managing Sprint.
 */
@RestController
@RequestMapping("/api")
public class SprintResource {

    private final Logger log = LoggerFactory.getLogger(SprintResource.class);

    private static final String ENTITY_NAME = "sprint";
        
    private final SprintService sprintService;

    public SprintResource(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    /**
     * POST  /sprints : Create a new sprint.
     *
     * @param sprintDTO the sprintDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sprintDTO, or with status 400 (Bad Request) if the sprint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sprints")
    @Timed
    public ResponseEntity<SprintDTO> createSprint(@Valid @RequestBody SprintDTO sprintDTO) throws URISyntaxException {
        log.debug("REST request to save Sprint : {}", sprintDTO);
        if (sprintDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sprint cannot already have an ID")).body(null);
        }
        SprintDTO result = sprintService.save(sprintDTO);
        return ResponseEntity.created(new URI("/api/sprints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sprints : Updates an existing sprint.
     *
     * @param sprintDTO the sprintDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sprintDTO,
     * or with status 400 (Bad Request) if the sprintDTO is not valid,
     * or with status 500 (Internal Server Error) if the sprintDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sprints")
    @Timed
    public ResponseEntity<SprintDTO> updateSprint(@Valid @RequestBody SprintDTO sprintDTO) throws URISyntaxException {
        log.debug("REST request to update Sprint : {}", sprintDTO);
        if (sprintDTO.getId() == null) {
            return createSprint(sprintDTO);
        }
        SprintDTO result = sprintService.save(sprintDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sprintDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sprints : get all the sprints.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sprints in body
     */
    @GetMapping("/sprints")
    @Timed
    public ResponseEntity<List<SprintDTO>> getAllSprints(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Sprints");
        Page<SprintDTO> page = sprintService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sprints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sprints/:id : get the "id" sprint.
     *
     * @param id the id of the sprintDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sprintDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sprints/{id}")
    @Timed
    public ResponseEntity<SprintDTO> getSprint(@PathVariable Long id) {
        log.debug("REST request to get Sprint : {}", id);
        SprintDTO sprintDTO = sprintService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sprintDTO));
    }

    /**
     * DELETE  /sprints/:id : delete the "id" sprint.
     *
     * @param id the id of the sprintDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sprints/{id}")
    @Timed
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id) {
        log.debug("REST request to delete Sprint : {}", id);
        sprintService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sprints?query=:query : search for the sprint corresponding
     * to the query.
     *
     * @param query the query of the sprint search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sprints")
    @Timed
    public ResponseEntity<List<SprintDTO>> searchSprints(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Sprints for query {}", query);
        Page<SprintDTO> page = sprintService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sprints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
