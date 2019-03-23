package com.nullpoint.musicroad.service;

import com.nullpoint.musicroad.domain.Band;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Band.
 */
public interface BandService {

    /**
     * Save a band.
     *
     * @param band the entity to save
     * @return the persisted entity
     */
    Band save(Band band);

    /**
     * Get all the bands.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Band> findAll(Pageable pageable);

    /**
     * Get all the Band with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<Band> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" band.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Band> findOne(Long id);

    /**
     * Delete the "id" band.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the band corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Band> search(String query, Pageable pageable);
}
