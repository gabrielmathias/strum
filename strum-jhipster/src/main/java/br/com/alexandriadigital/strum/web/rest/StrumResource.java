package br.com.alexandriadigital.strum.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.alexandriadigital.strum.service.StrumService;
import br.com.alexandriadigital.strum.web.rest.util.HeaderUtil;
import br.com.alexandriadigital.strum.web.rest.util.PaginationUtil;
import br.com.alexandriadigital.strum.service.dto.StrumDTO;
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
 * REST controller for managing Strum.
 */
@RestController
@RequestMapping("/api")
public class StrumResource {

    private final Logger log = LoggerFactory.getLogger(StrumResource.class);

    private static final String ENTITY_NAME = "strum";
        
    private final StrumService strumService;

    public StrumResource(StrumService strumService) {
        this.strumService = strumService;
    }

    /**
     * POST  /strums : Create a new strum.
     *
     * @param strumDTO the strumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new strumDTO, or with status 400 (Bad Request) if the strum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/strums")
    @Timed
    public ResponseEntity<StrumDTO> createStrum(@Valid @RequestBody StrumDTO strumDTO) throws URISyntaxException {
        log.debug("REST request to save Strum : {}", strumDTO);
        if (strumDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new strum cannot already have an ID")).body(null);
        }
        StrumDTO result = strumService.save(strumDTO);
        return ResponseEntity.created(new URI("/api/strums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /strums : Updates an existing strum.
     *
     * @param strumDTO the strumDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated strumDTO,
     * or with status 400 (Bad Request) if the strumDTO is not valid,
     * or with status 500 (Internal Server Error) if the strumDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/strums")
    @Timed
    public ResponseEntity<StrumDTO> updateStrum(@Valid @RequestBody StrumDTO strumDTO) throws URISyntaxException {
        log.debug("REST request to update Strum : {}", strumDTO);
        if (strumDTO.getId() == null) {
            return createStrum(strumDTO);
        }
        StrumDTO result = strumService.save(strumDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, strumDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /strums : get all the strums.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of strums in body
     */
    @GetMapping("/strums")
    @Timed
    public ResponseEntity<List<StrumDTO>> getAllStrums(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Strums");
        Page<StrumDTO> page = strumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/strums");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /strums/:id : get the "id" strum.
     *
     * @param id the id of the strumDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the strumDTO, or with status 404 (Not Found)
     */
    @GetMapping("/strums/{id}")
    @Timed
    public ResponseEntity<StrumDTO> getStrum(@PathVariable Long id) {
        log.debug("REST request to get Strum : {}", id);
        StrumDTO strumDTO = strumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(strumDTO));
    }

    /**
     * DELETE  /strums/:id : delete the "id" strum.
     *
     * @param id the id of the strumDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/strums/{id}")
    @Timed
    public ResponseEntity<Void> deleteStrum(@PathVariable Long id) {
        log.debug("REST request to delete Strum : {}", id);
        strumService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/strums?query=:query : search for the strum corresponding
     * to the query.
     *
     * @param query the query of the strum search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/strums")
    @Timed
    public ResponseEntity<List<StrumDTO>> searchStrums(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Strums for query {}", query);
        Page<StrumDTO> page = strumService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/strums");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
