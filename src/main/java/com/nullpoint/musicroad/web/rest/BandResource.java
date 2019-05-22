package com.nullpoint.musicroad.web.rest;

import com.nullpoint.musicroad.domain.Band;
import com.nullpoint.musicroad.service.BandService;
import com.nullpoint.musicroad.web.rest.errors.BadRequestAlertException;
import com.nullpoint.musicroad.web.rest.errors.ImageException;
import com.nullpoint.musicroad.web.rest.errors.NumberException;
import com.nullpoint.musicroad.web.rest.errors.YearException;
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Band.
 */
@RestController
@RequestMapping("/api")
public class BandResource {

    private final Logger log = LoggerFactory.getLogger(BandResource.class);

    private static final String ENTITY_NAME = "band";

    private final BandService bandService;

    public BandResource(BandService bandService) {
        this.bandService = bandService;
    }

    /**
     * POST  /bands : Create a new band.
     *
     * @param band the band to create
     * @return the ResponseEntity with status 201 (Created) and with body the new band, or with status 400 (Bad Request) if the band has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bands")
    public ResponseEntity<Band> createBand(@RequestBody Band band) throws URISyntaxException {
        log.debug("REST request to save Band : {}", band);
        if (band.getId() != null) {
            throw new BadRequestAlertException("A new band cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Band result = bandService.save(band);
        return ResponseEntity.created(new URI("/api/bands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bands : Updates an existing band.
     *
     * @param band the band to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated band,
     * or with status 400 (Bad Request) if the band is not valid,
     * or with status 500 (Internal Server Error) if the band couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bands")
    public ResponseEntity<Band> updateBand(@RequestBody Band band) throws URISyntaxException {
        log.debug("REST request to update Band : {}", band);
        if (band.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (checkYear(band.getCreationYear()) == false) {
            throw new YearException();
        }

        if (band.getCoverPicture() == null) {
            throw new ImageException();
        }
        if (band.getComponentNumber() < 1) {
            throw new NumberException();
        }
        Band result = bandService.save(band);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, band.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bands : get all the bands.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of bands in body
     */
    @GetMapping("/bands")
    public ResponseEntity<List<Band>> getAllBands(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Bands");
        Page<Band> page;
        if (eagerload) {
            page = bandService.findAllWithEagerRelationships(pageable);
        } else {
            page = bandService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/bands?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /bands/:id : get the "id" band.
     *
     * @param id the id of the band to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the band, or with status 404 (Not Found)
     */
    @GetMapping("/bands/{id}")
    public ResponseEntity<Band> getBand(@PathVariable Long id) {
        log.debug("REST request to get Band : {}", id);
        Optional<Band> band = bandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(band);
    }

    /**
     * DELETE  /bands/:id : delete the "id" band.
     *
     * @param id the id of the band to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bands/{id}")
    public ResponseEntity<Void> deleteBand(@PathVariable Long id) {
        log.debug("REST request to delete Band : {}", id);
        bandService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bands?query=:query : search for the band corresponding
     * to the query.
     *
     * @param query the query of the band search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bands")
    public ResponseEntity<List<Band>> searchBands(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Bands for query {}", query);
        Page<Band> page = bandService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bands");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @Deprecated
    private static boolean checkYear(int year) {
        boolean res = true;
        Date fechaActual = new GregorianCalendar().getTime();
        if (year > ((fechaActual.getYear()) + 1900)) {
            res = false;
        }
        return res;
    }

}
