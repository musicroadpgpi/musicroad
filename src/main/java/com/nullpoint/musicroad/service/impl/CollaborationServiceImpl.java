package com.nullpoint.musicroad.service.impl;

import com.nullpoint.musicroad.service.CollaborationService;
import com.nullpoint.musicroad.domain.Collaboration;
import com.nullpoint.musicroad.repository.CollaborationRepository;
import com.nullpoint.musicroad.repository.search.CollaborationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Collaboration.
 */
@Service
@Transactional
public class CollaborationServiceImpl implements CollaborationService {

    private final Logger log = LoggerFactory.getLogger(CollaborationServiceImpl.class);

    private final CollaborationRepository collaborationRepository;

    private final CollaborationSearchRepository collaborationSearchRepository;

    public CollaborationServiceImpl(CollaborationRepository collaborationRepository, CollaborationSearchRepository collaborationSearchRepository) {
        this.collaborationRepository = collaborationRepository;
        this.collaborationSearchRepository = collaborationSearchRepository;
    }

    /**
     * Save a collaboration.
     *
     * @param collaboration the entity to save
     * @return the persisted entity
     */
    @Override
    public Collaboration save(Collaboration collaboration) {
        log.debug("Request to save Collaboration : {}", collaboration);
        Collaboration result = collaborationRepository.save(collaboration);
        collaborationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the collaborations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Collaboration> findAll(Pageable pageable) {
        log.debug("Request to get all Collaborations");
        return collaborationRepository.findAll(pageable);
    }


    /**
     * Get one collaboration by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Collaboration> findOne(Long id) {
        log.debug("Request to get Collaboration : {}", id);
        return collaborationRepository.findById(id);
    }

    /**
     * Delete the collaboration by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Collaboration : {}", id);
        collaborationRepository.deleteById(id);
        collaborationSearchRepository.deleteById(id);
    }

    /**
     * Search for the collaboration corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Collaboration> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Collaborations for query {}", query);
        return collaborationSearchRepository.search(queryStringQuery(query), pageable);    }
}
