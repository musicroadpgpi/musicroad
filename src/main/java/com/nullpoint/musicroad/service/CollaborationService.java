package com.nullpoint.musicroad.service;

import com.nullpoint.musicroad.domain.Collaboration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Collaboration.
 */
public interface CollaborationService {

    /**
     * Save a collaboration.
     *
     * @param collaboration the entity to save
     * @return the persisted entity
     */
    Collaboration save(Collaboration collaboration);

    /**
     * Get all the collaborations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Collaboration> findAll(Pageable pageable);


    /**
     * Get the "id" collaboration.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Collaboration> findOne(Long id);

    /**
     * Delete the "id" collaboration.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the collaboration corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Collaboration> search(String query, Pageable pageable);
}
