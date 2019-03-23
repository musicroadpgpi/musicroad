package com.nullpoint.musicroad.web.rest;

import com.nullpoint.musicroad.MusicroadApp;

import com.nullpoint.musicroad.domain.Band;
import com.nullpoint.musicroad.repository.BandRepository;
import com.nullpoint.musicroad.repository.search.BandSearchRepository;
import com.nullpoint.musicroad.service.BandService;
import com.nullpoint.musicroad.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.nullpoint.musicroad.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullpoint.musicroad.domain.enumeration.Genre;
/**
 * Test class for the BandResource REST controller.
 *
 * @see BandResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MusicroadApp.class)
public class BandResourceIntTest {

    private static final String DEFAULT_BAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BAND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_COVER_PICTURE = "BBBBBBBBBB";

    private static final Integer DEFAULT_COMPONENT_NUMBER = 1;
    private static final Integer UPDATED_COMPONENT_NUMBER = 2;

    private static final Integer DEFAULT_CREATION_YEAR = 1;
    private static final Integer UPDATED_CREATION_YEAR = 2;

    private static final Genre DEFAULT_GENRE = Genre.Rock;
    private static final Genre UPDATED_GENRE = Genre.RandB;

    @Autowired
    private BandRepository bandRepository;

    @Mock
    private BandRepository bandRepositoryMock;

    @Mock
    private BandService bandServiceMock;

    @Autowired
    private BandService bandService;

    /**
     * This repository is mocked in the com.nullpoint.musicroad.repository.search test package.
     *
     * @see com.nullpoint.musicroad.repository.search.BandSearchRepositoryMockConfiguration
     */
    @Autowired
    private BandSearchRepository mockBandSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restBandMockMvc;

    private Band band;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BandResource bandResource = new BandResource(bandService);
        this.restBandMockMvc = MockMvcBuilders.standaloneSetup(bandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Band createEntity(EntityManager em) {
        Band band = new Band()
            .bandName(DEFAULT_BAND_NAME)
            .bio(DEFAULT_BIO)
            .coverPicture(DEFAULT_COVER_PICTURE)
            .componentNumber(DEFAULT_COMPONENT_NUMBER)
            .creationYear(DEFAULT_CREATION_YEAR)
            .genre(DEFAULT_GENRE);
        return band;
    }

    @Before
    public void initTest() {
        band = createEntity(em);
    }

    @Test
    @Transactional
    public void createBand() throws Exception {
        int databaseSizeBeforeCreate = bandRepository.findAll().size();

        // Create the Band
        restBandMockMvc.perform(post("/api/bands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(band)))
            .andExpect(status().isCreated());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeCreate + 1);
        Band testBand = bandList.get(bandList.size() - 1);
        assertThat(testBand.getBandName()).isEqualTo(DEFAULT_BAND_NAME);
        assertThat(testBand.getBio()).isEqualTo(DEFAULT_BIO);
        assertThat(testBand.getCoverPicture()).isEqualTo(DEFAULT_COVER_PICTURE);
        assertThat(testBand.getComponentNumber()).isEqualTo(DEFAULT_COMPONENT_NUMBER);
        assertThat(testBand.getCreationYear()).isEqualTo(DEFAULT_CREATION_YEAR);
        assertThat(testBand.getGenre()).isEqualTo(DEFAULT_GENRE);

        // Validate the Band in Elasticsearch
        verify(mockBandSearchRepository, times(1)).save(testBand);
    }

    @Test
    @Transactional
    public void createBandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bandRepository.findAll().size();

        // Create the Band with an existing ID
        band.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBandMockMvc.perform(post("/api/bands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(band)))
            .andExpect(status().isBadRequest());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeCreate);

        // Validate the Band in Elasticsearch
        verify(mockBandSearchRepository, times(0)).save(band);
    }

    @Test
    @Transactional
    public void getAllBands() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList
        restBandMockMvc.perform(get("/api/bands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(band.getId().intValue())))
            .andExpect(jsonPath("$.[*].bandName").value(hasItem(DEFAULT_BAND_NAME.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())))
            .andExpect(jsonPath("$.[*].coverPicture").value(hasItem(DEFAULT_COVER_PICTURE.toString())))
            .andExpect(jsonPath("$.[*].componentNumber").value(hasItem(DEFAULT_COMPONENT_NUMBER)))
            .andExpect(jsonPath("$.[*].creationYear").value(hasItem(DEFAULT_CREATION_YEAR)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllBandsWithEagerRelationshipsIsEnabled() throws Exception {
        BandResource bandResource = new BandResource(bandServiceMock);
        when(bandServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restBandMockMvc = MockMvcBuilders.standaloneSetup(bandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restBandMockMvc.perform(get("/api/bands?eagerload=true"))
        .andExpect(status().isOk());

        verify(bandServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllBandsWithEagerRelationshipsIsNotEnabled() throws Exception {
        BandResource bandResource = new BandResource(bandServiceMock);
            when(bandServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restBandMockMvc = MockMvcBuilders.standaloneSetup(bandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restBandMockMvc.perform(get("/api/bands?eagerload=true"))
        .andExpect(status().isOk());

            verify(bandServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getBand() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get the band
        restBandMockMvc.perform(get("/api/bands/{id}", band.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(band.getId().intValue()))
            .andExpect(jsonPath("$.bandName").value(DEFAULT_BAND_NAME.toString()))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO.toString()))
            .andExpect(jsonPath("$.coverPicture").value(DEFAULT_COVER_PICTURE.toString()))
            .andExpect(jsonPath("$.componentNumber").value(DEFAULT_COMPONENT_NUMBER))
            .andExpect(jsonPath("$.creationYear").value(DEFAULT_CREATION_YEAR))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBand() throws Exception {
        // Get the band
        restBandMockMvc.perform(get("/api/bands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBand() throws Exception {
        // Initialize the database
        bandService.save(band);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockBandSearchRepository);

        int databaseSizeBeforeUpdate = bandRepository.findAll().size();

        // Update the band
        Band updatedBand = bandRepository.findById(band.getId()).get();
        // Disconnect from session so that the updates on updatedBand are not directly saved in db
        em.detach(updatedBand);
        updatedBand
            .bandName(UPDATED_BAND_NAME)
            .bio(UPDATED_BIO)
            .coverPicture(UPDATED_COVER_PICTURE)
            .componentNumber(UPDATED_COMPONENT_NUMBER)
            .creationYear(UPDATED_CREATION_YEAR)
            .genre(UPDATED_GENRE);

        restBandMockMvc.perform(put("/api/bands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBand)))
            .andExpect(status().isOk());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);
        Band testBand = bandList.get(bandList.size() - 1);
        assertThat(testBand.getBandName()).isEqualTo(UPDATED_BAND_NAME);
        assertThat(testBand.getBio()).isEqualTo(UPDATED_BIO);
        assertThat(testBand.getCoverPicture()).isEqualTo(UPDATED_COVER_PICTURE);
        assertThat(testBand.getComponentNumber()).isEqualTo(UPDATED_COMPONENT_NUMBER);
        assertThat(testBand.getCreationYear()).isEqualTo(UPDATED_CREATION_YEAR);
        assertThat(testBand.getGenre()).isEqualTo(UPDATED_GENRE);

        // Validate the Band in Elasticsearch
        verify(mockBandSearchRepository, times(1)).save(testBand);
    }

    @Test
    @Transactional
    public void updateNonExistingBand() throws Exception {
        int databaseSizeBeforeUpdate = bandRepository.findAll().size();

        // Create the Band

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBandMockMvc.perform(put("/api/bands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(band)))
            .andExpect(status().isBadRequest());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Band in Elasticsearch
        verify(mockBandSearchRepository, times(0)).save(band);
    }

    @Test
    @Transactional
    public void deleteBand() throws Exception {
        // Initialize the database
        bandService.save(band);

        int databaseSizeBeforeDelete = bandRepository.findAll().size();

        // Delete the band
        restBandMockMvc.perform(delete("/api/bands/{id}", band.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Band in Elasticsearch
        verify(mockBandSearchRepository, times(1)).deleteById(band.getId());
    }

    @Test
    @Transactional
    public void searchBand() throws Exception {
        // Initialize the database
        bandService.save(band);
        when(mockBandSearchRepository.search(queryStringQuery("id:" + band.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(band), PageRequest.of(0, 1), 1));
        // Search the band
        restBandMockMvc.perform(get("/api/_search/bands?query=id:" + band.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(band.getId().intValue())))
            .andExpect(jsonPath("$.[*].bandName").value(hasItem(DEFAULT_BAND_NAME)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)))
            .andExpect(jsonPath("$.[*].coverPicture").value(hasItem(DEFAULT_COVER_PICTURE)))
            .andExpect(jsonPath("$.[*].componentNumber").value(hasItem(DEFAULT_COMPONENT_NUMBER)))
            .andExpect(jsonPath("$.[*].creationYear").value(hasItem(DEFAULT_CREATION_YEAR)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Band.class);
        Band band1 = new Band();
        band1.setId(1L);
        Band band2 = new Band();
        band2.setId(band1.getId());
        assertThat(band1).isEqualTo(band2);
        band2.setId(2L);
        assertThat(band1).isNotEqualTo(band2);
        band1.setId(null);
        assertThat(band1).isNotEqualTo(band2);
    }
}
