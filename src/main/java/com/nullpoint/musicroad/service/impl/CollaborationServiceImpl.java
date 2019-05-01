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

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.nullpoint.musicroad.service.impl.BandServiceImpl;
import com.nullpoint.musicroad.domain.Band;
import com.nullpoint.musicroad.repository.BandRepository;

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

    @Autowired
    private BandServiceImpl bandServiceImpl;

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
        System.out.println("LA MEHOH COLLAB : " + collaboration.getBands().toString());
        Collaboration result = collaborationRepository.save(collaboration);
        collaborationSearchRepository.save(result);
        collaborationRepository.flush();
        for (Band band : collaboration.getBands()) {
            Set<Collaboration> collaborations = band.getCollaborations();
            collaborations.add(collaboration);
            band.setCollaborations(collaborations);
            Band savedBand = bandServiceImpl.saveForCollaboration(band);
            System.out.println(savedBand);
            System.out.println(savedBand.getCollaborations());
        }
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
        System.out.println("Las VANDAS de las collabs : " + collaborationRepository.findById(id).get().getBands().toString());
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
