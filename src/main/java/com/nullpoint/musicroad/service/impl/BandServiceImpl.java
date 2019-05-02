package com.nullpoint.musicroad.service.impl;

import com.nullpoint.musicroad.service.BandService;
import com.nullpoint.musicroad.domain.Band;
import com.nullpoint.musicroad.repository.BandRepository;
import com.nullpoint.musicroad.repository.search.BandSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nullpoint.musicroad.security.SecurityUtils;
import org.springframework.util.Assert;

import java.util.Optional;
import com.nullpoint.musicroad.domain.User;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Band.
 */
@Service
@Transactional
public class BandServiceImpl implements BandService {

    private final Logger log = LoggerFactory.getLogger(BandServiceImpl.class);

    private final BandRepository bandRepository;

    private final BandSearchRepository bandSearchRepository;

    public BandServiceImpl(BandRepository bandRepository, BandSearchRepository bandSearchRepository) {
        this.bandRepository = bandRepository;
        this.bandSearchRepository = bandSearchRepository;
    }

    /**
     * Save a band.
     *
     * @param band the entity to save
     * @return the persisted entity
     */
    @Override
    public Band save(Band band) {
        log.debug("Request to save Band : {}", band);
        Band storedBand = null;
        if (band.getId() != null)
            storedBand = this.findOne(band.getId()).get();
        String principalUsername = SecurityUtils.getCurrentUserLogin().get();
        if ( storedBand != null ) {
            String storedUsername = storedBand.getUser().getLogin();
            // No se pueden modificar las collaborations al editar la banda
            band.setCollaborations(storedBand.getCollaborations());
            Assert.isTrue(storedUsername.equals(principalUsername));
            Assert.isTrue(band.getUser().getLogin().equals(principalUsername));
        }
        Band result = bandRepository.save(band);
        bandSearchRepository.save(result);
        return result;
    }

    public Band saveForCollaboration(Band band) {
        log.debug("Request to save Band : {}", band);
        Band result = bandRepository.save(band);
        bandSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the bands.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Band> findAll(Pageable pageable) {
        log.debug("Request to get all Bands");
        return bandRepository.findAll(pageable);
    }

    /**
     * Get all the Band with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Band> findAllWithEagerRelationships(Pageable pageable) {
        return bandRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one band by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Band> findOne(Long id) {
        log.debug("Request to get Band : {}", id);
        return bandRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the band by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Band : {}", id);
        Band storedBand = this.findOne(id).get();
        String principalUsername = SecurityUtils.getCurrentUserLogin().get();
        if ( storedBand != null ) {
            String storedUsername = storedBand.getUser().getLogin();
            Assert.isTrue(storedUsername.equals(principalUsername));
        }
        bandRepository.deleteById(id);
        bandSearchRepository.deleteById(id);
    }

    /**
     * Search for the band corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Band> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Bands for query {}", query);
        return bandSearchRepository.search(queryStringQuery(query), pageable);    }
}
