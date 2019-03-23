package com.nullpoint.musicroad.web.rest;
import com.nullpoint.musicroad.domain.Collaboration;
import com.nullpoint.musicroad.service.CollaborationService;
import com.nullpoint.musicroad.web.rest.errors.BadRequestAlertException;
import com.nullpoint.musicroad.web.rest.util.HeaderUtil;
import com.nullpoint.musicroad.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Collaboration.
 */
@RestController
@RequestMapping("/api")
public class CollaborationResource {

    private final Logger log = LoggerFactory.getLogger(CollaborationResource.class);

    private static final String ENTITY_NAME = "collaboration";

    private final CollaborationService collaborationService;

    public CollaborationResource(CollaborationService collaborationService) {
        this.collaborationService = collaborationService;
    }

    /**
     * POST  /collaborations : Create a new collaboration.
     *
     * @param collaboration the collaboration to create
     * @return the ResponseEntity with status 201 (Created) and with body the new collaboration, or with status 400 (Bad Request) if the collaboration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/collaborations")
    public ResponseEntity<Collaboration> createCollaboration(@RequestBody Collaboration collaboration) throws URISyntaxException {
        log.debug("REST request to save Collaboration : {}", collaboration);
        if (collaboration.getId() != null) {
            throw new BadRequestAlertException("A new collaboration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Collaboration result = collaborationService.save(collaboration);
        return ResponseEntity.created(new URI("/api/collaborations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /collaborations : Updates an existing collaboration.
     *
     * @param collaboration the collaboration to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated collaboration,
     * or with status 400 (Bad Request) if the collaboration is not valid,
     * or with status 500 (Internal Server Error) if the collaboration couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/collaborations")
    public ResponseEntity<Collaboration> updateCollaboration(@RequestBody Collaboration collaboration) throws URISyntaxException {
        log.debug("REST request to update Collaboration : {}", collaboration);
        if (collaboration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Collaboration result = collaborationService.save(collaboration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, collaboration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /collaborations : get all the collaborations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of collaborations in body
     */
    @GetMapping("/collaborations")
    public ResponseEntity<List<Collaboration>> getAllCollaborations(Pageable pageable) {
        log.debug("REST request to get a page of Collaborations");
        Page<Collaboration> page = collaborationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/collaborations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /collaborations/:id : get the "id" collaboration.
     *
     * @param id the id of the collaboration to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the collaboration, or with status 404 (Not Found)
     */
    @GetMapping("/collaborations/{id}")
    public ResponseEntity<Collaboration> getCollaboration(@PathVariable Long id) {
        log.debug("REST request to get Collaboration : {}", id);
        Optional<Collaboration> collaboration = collaborationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collaboration);
    }

    /**
     * DELETE  /collaborations/:id : delete the "id" collaboration.
     *
     * @param id the id of the collaboration to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/collaborations/{id}")
    public ResponseEntity<Void> deleteCollaboration(@PathVariable Long id) {
        log.debug("REST request to delete Collaboration : {}", id);
        collaborationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/collaborations?query=:query : search for the collaboration corresponding
     * to the query.
     *
     * @param query the query of the collaboration search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/collaborations")
    public ResponseEntity<List<Collaboration>> searchCollaborations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Collaborations for query {}", query);
        Page<Collaboration> page = collaborationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/collaborations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
