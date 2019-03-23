package com.nullpoint.musicroad.web.rest;

import com.nullpoint.musicroad.MusicroadApp;

import com.nullpoint.musicroad.domain.Collaboration;
import com.nullpoint.musicroad.repository.CollaborationRepository;
import com.nullpoint.musicroad.repository.search.CollaborationSearchRepository;
import com.nullpoint.musicroad.service.CollaborationService;
import com.nullpoint.musicroad.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.nullpoint.musicroad.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CollaborationResource REST controller.
 *
 * @see CollaborationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MusicroadApp.class)
public class CollaborationResourceIntTest {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PROPOSED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROPOSED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CollaborationRepository collaborationRepository;

    @Autowired
    private CollaborationService collaborationService;

    /**
     * This repository is mocked in the com.nullpoint.musicroad.repository.search test package.
     *
     * @see com.nullpoint.musicroad.repository.search.CollaborationSearchRepositoryMockConfiguration
     */
    @Autowired
    private CollaborationSearchRepository mockCollaborationSearchRepository;

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

    private MockMvc restCollaborationMockMvc;

    private Collaboration collaboration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CollaborationResource collaborationResource = new CollaborationResource(collaborationService);
        this.restCollaborationMockMvc = MockMvcBuilders.standaloneSetup(collaborationResource)
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
    public static Collaboration createEntity(EntityManager em) {
        Collaboration collaboration = new Collaboration()
            .message(DEFAULT_MESSAGE)
            .proposedDate(DEFAULT_PROPOSED_DATE);
        return collaboration;
    }

    @Before
    public void initTest() {
        collaboration = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollaboration() throws Exception {
        int databaseSizeBeforeCreate = collaborationRepository.findAll().size();

        // Create the Collaboration
        restCollaborationMockMvc.perform(post("/api/collaborations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboration)))
            .andExpect(status().isCreated());

        // Validate the Collaboration in the database
        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeCreate + 1);
        Collaboration testCollaboration = collaborationList.get(collaborationList.size() - 1);
        assertThat(testCollaboration.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testCollaboration.getProposedDate()).isEqualTo(DEFAULT_PROPOSED_DATE);

        // Validate the Collaboration in Elasticsearch
        verify(mockCollaborationSearchRepository, times(1)).save(testCollaboration);
    }

    @Test
    @Transactional
    public void createCollaborationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = collaborationRepository.findAll().size();

        // Create the Collaboration with an existing ID
        collaboration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollaborationMockMvc.perform(post("/api/collaborations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboration)))
            .andExpect(status().isBadRequest());

        // Validate the Collaboration in the database
        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Collaboration in Elasticsearch
        verify(mockCollaborationSearchRepository, times(0)).save(collaboration);
    }

    @Test
    @Transactional
    public void getAllCollaborations() throws Exception {
        // Initialize the database
        collaborationRepository.saveAndFlush(collaboration);

        // Get all the collaborationList
        restCollaborationMockMvc.perform(get("/api/collaborations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collaboration.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].proposedDate").value(hasItem(DEFAULT_PROPOSED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getCollaboration() throws Exception {
        // Initialize the database
        collaborationRepository.saveAndFlush(collaboration);

        // Get the collaboration
        restCollaborationMockMvc.perform(get("/api/collaborations/{id}", collaboration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(collaboration.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.proposedDate").value(DEFAULT_PROPOSED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCollaboration() throws Exception {
        // Get the collaboration
        restCollaborationMockMvc.perform(get("/api/collaborations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollaboration() throws Exception {
        // Initialize the database
        collaborationService.save(collaboration);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCollaborationSearchRepository);

        int databaseSizeBeforeUpdate = collaborationRepository.findAll().size();

        // Update the collaboration
        Collaboration updatedCollaboration = collaborationRepository.findById(collaboration.getId()).get();
        // Disconnect from session so that the updates on updatedCollaboration are not directly saved in db
        em.detach(updatedCollaboration);
        updatedCollaboration
            .message(UPDATED_MESSAGE)
            .proposedDate(UPDATED_PROPOSED_DATE);

        restCollaborationMockMvc.perform(put("/api/collaborations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCollaboration)))
            .andExpect(status().isOk());

        // Validate the Collaboration in the database
        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeUpdate);
        Collaboration testCollaboration = collaborationList.get(collaborationList.size() - 1);
        assertThat(testCollaboration.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testCollaboration.getProposedDate()).isEqualTo(UPDATED_PROPOSED_DATE);

        // Validate the Collaboration in Elasticsearch
        verify(mockCollaborationSearchRepository, times(1)).save(testCollaboration);
    }

    @Test
    @Transactional
    public void updateNonExistingCollaboration() throws Exception {
        int databaseSizeBeforeUpdate = collaborationRepository.findAll().size();

        // Create the Collaboration

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollaborationMockMvc.perform(put("/api/collaborations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboration)))
            .andExpect(status().isBadRequest());

        // Validate the Collaboration in the database
        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Collaboration in Elasticsearch
        verify(mockCollaborationSearchRepository, times(0)).save(collaboration);
    }

    @Test
    @Transactional
    public void deleteCollaboration() throws Exception {
        // Initialize the database
        collaborationService.save(collaboration);

        int databaseSizeBeforeDelete = collaborationRepository.findAll().size();

        // Delete the collaboration
        restCollaborationMockMvc.perform(delete("/api/collaborations/{id}", collaboration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Collaboration in Elasticsearch
        verify(mockCollaborationSearchRepository, times(1)).deleteById(collaboration.getId());
    }

    @Test
    @Transactional
    public void searchCollaboration() throws Exception {
        // Initialize the database
        collaborationService.save(collaboration);
        when(mockCollaborationSearchRepository.search(queryStringQuery("id:" + collaboration.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(collaboration), PageRequest.of(0, 1), 1));
        // Search the collaboration
        restCollaborationMockMvc.perform(get("/api/_search/collaborations?query=id:" + collaboration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collaboration.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].proposedDate").value(hasItem(DEFAULT_PROPOSED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Collaboration.class);
        Collaboration collaboration1 = new Collaboration();
        collaboration1.setId(1L);
        Collaboration collaboration2 = new Collaboration();
        collaboration2.setId(collaboration1.getId());
        assertThat(collaboration1).isEqualTo(collaboration2);
        collaboration2.setId(2L);
        assertThat(collaboration1).isNotEqualTo(collaboration2);
        collaboration1.setId(null);
        assertThat(collaboration1).isNotEqualTo(collaboration2);
    }
}
