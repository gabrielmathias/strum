package br.com.alexandriadigital.strum.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.alexandriadigital.strum.service.AcceptanceService;
import br.com.alexandriadigital.strum.web.rest.util.HeaderUtil;
import br.com.alexandriadigital.strum.web.rest.util.PaginationUtil;
import br.com.alexandriadigital.strum.service.dto.AcceptanceDTO;
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
 * REST controller for managing Acceptance.
 */
@RestController
@RequestMapping("/api")
public class AcceptanceResource {

    private final Logger log = LoggerFactory.getLogger(AcceptanceResource.class);

    private static final String ENTITY_NAME = "acceptance";
        
    private final AcceptanceService acceptanceService;

    public AcceptanceResource(AcceptanceService acceptanceService) {
        this.acceptanceService = acceptanceService;
    }

    /**
     * POST  /acceptances : Create a new acceptance.
     *
     * @param acceptanceDTO the acceptanceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new acceptanceDTO, or with status 400 (Bad Request) if the acceptance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/acceptances")
    @Timed
    public ResponseEntity<AcceptanceDTO> createAcceptance(@Valid @RequestBody AcceptanceDTO acceptanceDTO) throws URISyntaxException {
        log.debug("REST request to save Acceptance : {}", acceptanceDTO);
        if (acceptanceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new acceptance cannot already have an ID")).body(null);
        }
        AcceptanceDTO result = acceptanceService.save(acceptanceDTO);
        return ResponseEntity.created(new URI("/api/acceptances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /acceptances : Updates an existing acceptance.
     *
     * @param acceptanceDTO the acceptanceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated acceptanceDTO,
     * or with status 400 (Bad Request) if the acceptanceDTO is not valid,
     * or with status 500 (Internal Server Error) if the acceptanceDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/acceptances")
    @Timed
    public ResponseEntity<AcceptanceDTO> updateAcceptance(@Valid @RequestBody AcceptanceDTO acceptanceDTO) throws URISyntaxException {
        log.debug("REST request to update Acceptance : {}", acceptanceDTO);
        if (acceptanceDTO.getId() == null) {
            return createAcceptance(acceptanceDTO);
        }
        AcceptanceDTO result = acceptanceService.save(acceptanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, acceptanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /acceptances : get all the acceptances.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of acceptances in body
     */
    @GetMapping("/acceptances")
    @Timed
    public ResponseEntity<List<AcceptanceDTO>> getAllAcceptances(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Acceptances");
        Page<AcceptanceDTO> page = acceptanceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/acceptances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /acceptances/:id : get the "id" acceptance.
     *
     * @param id the id of the acceptanceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the acceptanceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/acceptances/{id}")
    @Timed
    public ResponseEntity<AcceptanceDTO> getAcceptance(@PathVariable Long id) {
        log.debug("REST request to get Acceptance : {}", id);
        AcceptanceDTO acceptanceDTO = acceptanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(acceptanceDTO));
    }

    /**
     * DELETE  /acceptances/:id : delete the "id" acceptance.
     *
     * @param id the id of the acceptanceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/acceptances/{id}")
    @Timed
    public ResponseEntity<Void> deleteAcceptance(@PathVariable Long id) {
        log.debug("REST request to delete Acceptance : {}", id);
        acceptanceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/acceptances?query=:query : search for the acceptance corresponding
     * to the query.
     *
     * @param query the query of the acceptance search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/acceptances")
    @Timed
    public ResponseEntity<List<AcceptanceDTO>> searchAcceptances(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Acceptances for query {}", query);
        Page<AcceptanceDTO> page = acceptanceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/acceptances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
