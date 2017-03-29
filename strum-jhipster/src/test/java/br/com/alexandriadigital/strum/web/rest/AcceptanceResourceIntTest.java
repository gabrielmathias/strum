package br.com.alexandriadigital.strum.web.rest;

import br.com.alexandriadigital.strum.StrumApp;

import br.com.alexandriadigital.strum.domain.Acceptance;
import br.com.alexandriadigital.strum.domain.User;
import br.com.alexandriadigital.strum.domain.Story;
import br.com.alexandriadigital.strum.repository.AcceptanceRepository;
import br.com.alexandriadigital.strum.service.AcceptanceService;
import br.com.alexandriadigital.strum.repository.search.AcceptanceSearchRepository;
import br.com.alexandriadigital.strum.service.dto.AcceptanceDTO;
import br.com.alexandriadigital.strum.service.mapper.AcceptanceMapper;
import br.com.alexandriadigital.strum.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.alexandriadigital.strum.domain.enumeration.AcceptanceStatus;
/**
 * Test class for the AcceptanceResource REST controller.
 *
 * @see AcceptanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StrumApp.class)
public class AcceptanceResourceIntTest {

    private static final String DEFAULT_CRITERIA = "AAAAAAAAAA";
    private static final String UPDATED_CRITERIA = "BBBBBBBBBB";

    private static final AcceptanceStatus DEFAULT_STATUS = AcceptanceStatus.ACCEPTED;
    private static final AcceptanceStatus UPDATED_STATUS = AcceptanceStatus.REJECTED;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private AcceptanceRepository acceptanceRepository;

    @Autowired
    private AcceptanceMapper acceptanceMapper;

    @Autowired
    private AcceptanceService acceptanceService;

    @Autowired
    private AcceptanceSearchRepository acceptanceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAcceptanceMockMvc;

    private Acceptance acceptance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AcceptanceResource acceptanceResource = new AcceptanceResource(acceptanceService);
        this.restAcceptanceMockMvc = MockMvcBuilders.standaloneSetup(acceptanceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acceptance createEntity(EntityManager em) {
        Acceptance acceptance = new Acceptance()
            .criteria(DEFAULT_CRITERIA)
            .status(DEFAULT_STATUS)
            .message(DEFAULT_MESSAGE);
        // Add required entity
        User created_by = UserResourceIntTest.createEntity(em);
        em.persist(created_by);
        em.flush();
        acceptance.setCreated_by(created_by);
        // Add required entity
        Story story = StoryResourceIntTest.createEntity(em);
        em.persist(story);
        em.flush();
        acceptance.setStory(story);
        return acceptance;
    }

    @Before
    public void initTest() {
        acceptanceSearchRepository.deleteAll();
        acceptance = createEntity(em);
    }

    @Test
    @Transactional
    public void createAcceptance() throws Exception {
        int databaseSizeBeforeCreate = acceptanceRepository.findAll().size();

        // Create the Acceptance
        AcceptanceDTO acceptanceDTO = acceptanceMapper.acceptanceToAcceptanceDTO(acceptance);
        restAcceptanceMockMvc.perform(post("/api/acceptances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acceptanceDTO)))
            .andExpect(status().isCreated());

        // Validate the Acceptance in the database
        List<Acceptance> acceptanceList = acceptanceRepository.findAll();
        assertThat(acceptanceList).hasSize(databaseSizeBeforeCreate + 1);
        Acceptance testAcceptance = acceptanceList.get(acceptanceList.size() - 1);
        assertThat(testAcceptance.getCriteria()).isEqualTo(DEFAULT_CRITERIA);
        assertThat(testAcceptance.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAcceptance.getMessage()).isEqualTo(DEFAULT_MESSAGE);

        // Validate the Acceptance in Elasticsearch
        Acceptance acceptanceEs = acceptanceSearchRepository.findOne(testAcceptance.getId());
        assertThat(acceptanceEs).isEqualToComparingFieldByField(testAcceptance);
    }

    @Test
    @Transactional
    public void createAcceptanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = acceptanceRepository.findAll().size();

        // Create the Acceptance with an existing ID
        acceptance.setId(1L);
        AcceptanceDTO acceptanceDTO = acceptanceMapper.acceptanceToAcceptanceDTO(acceptance);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcceptanceMockMvc.perform(post("/api/acceptances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acceptanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Acceptance> acceptanceList = acceptanceRepository.findAll();
        assertThat(acceptanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCriteriaIsRequired() throws Exception {
        int databaseSizeBeforeTest = acceptanceRepository.findAll().size();
        // set the field null
        acceptance.setCriteria(null);

        // Create the Acceptance, which fails.
        AcceptanceDTO acceptanceDTO = acceptanceMapper.acceptanceToAcceptanceDTO(acceptance);

        restAcceptanceMockMvc.perform(post("/api/acceptances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acceptanceDTO)))
            .andExpect(status().isBadRequest());

        List<Acceptance> acceptanceList = acceptanceRepository.findAll();
        assertThat(acceptanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = acceptanceRepository.findAll().size();
        // set the field null
        acceptance.setStatus(null);

        // Create the Acceptance, which fails.
        AcceptanceDTO acceptanceDTO = acceptanceMapper.acceptanceToAcceptanceDTO(acceptance);

        restAcceptanceMockMvc.perform(post("/api/acceptances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acceptanceDTO)))
            .andExpect(status().isBadRequest());

        List<Acceptance> acceptanceList = acceptanceRepository.findAll();
        assertThat(acceptanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAcceptances() throws Exception {
        // Initialize the database
        acceptanceRepository.saveAndFlush(acceptance);

        // Get all the acceptanceList
        restAcceptanceMockMvc.perform(get("/api/acceptances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acceptance.getId().intValue())))
            .andExpect(jsonPath("$.[*].criteria").value(hasItem(DEFAULT_CRITERIA.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void getAcceptance() throws Exception {
        // Initialize the database
        acceptanceRepository.saveAndFlush(acceptance);

        // Get the acceptance
        restAcceptanceMockMvc.perform(get("/api/acceptances/{id}", acceptance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(acceptance.getId().intValue()))
            .andExpect(jsonPath("$.criteria").value(DEFAULT_CRITERIA.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAcceptance() throws Exception {
        // Get the acceptance
        restAcceptanceMockMvc.perform(get("/api/acceptances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAcceptance() throws Exception {
        // Initialize the database
        acceptanceRepository.saveAndFlush(acceptance);
        acceptanceSearchRepository.save(acceptance);
        int databaseSizeBeforeUpdate = acceptanceRepository.findAll().size();

        // Update the acceptance
        Acceptance updatedAcceptance = acceptanceRepository.findOne(acceptance.getId());
        updatedAcceptance
            .criteria(UPDATED_CRITERIA)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE);
        AcceptanceDTO acceptanceDTO = acceptanceMapper.acceptanceToAcceptanceDTO(updatedAcceptance);

        restAcceptanceMockMvc.perform(put("/api/acceptances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acceptanceDTO)))
            .andExpect(status().isOk());

        // Validate the Acceptance in the database
        List<Acceptance> acceptanceList = acceptanceRepository.findAll();
        assertThat(acceptanceList).hasSize(databaseSizeBeforeUpdate);
        Acceptance testAcceptance = acceptanceList.get(acceptanceList.size() - 1);
        assertThat(testAcceptance.getCriteria()).isEqualTo(UPDATED_CRITERIA);
        assertThat(testAcceptance.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAcceptance.getMessage()).isEqualTo(UPDATED_MESSAGE);

        // Validate the Acceptance in Elasticsearch
        Acceptance acceptanceEs = acceptanceSearchRepository.findOne(testAcceptance.getId());
        assertThat(acceptanceEs).isEqualToComparingFieldByField(testAcceptance);
    }

    @Test
    @Transactional
    public void updateNonExistingAcceptance() throws Exception {
        int databaseSizeBeforeUpdate = acceptanceRepository.findAll().size();

        // Create the Acceptance
        AcceptanceDTO acceptanceDTO = acceptanceMapper.acceptanceToAcceptanceDTO(acceptance);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAcceptanceMockMvc.perform(put("/api/acceptances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(acceptanceDTO)))
            .andExpect(status().isCreated());

        // Validate the Acceptance in the database
        List<Acceptance> acceptanceList = acceptanceRepository.findAll();
        assertThat(acceptanceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAcceptance() throws Exception {
        // Initialize the database
        acceptanceRepository.saveAndFlush(acceptance);
        acceptanceSearchRepository.save(acceptance);
        int databaseSizeBeforeDelete = acceptanceRepository.findAll().size();

        // Get the acceptance
        restAcceptanceMockMvc.perform(delete("/api/acceptances/{id}", acceptance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean acceptanceExistsInEs = acceptanceSearchRepository.exists(acceptance.getId());
        assertThat(acceptanceExistsInEs).isFalse();

        // Validate the database is empty
        List<Acceptance> acceptanceList = acceptanceRepository.findAll();
        assertThat(acceptanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAcceptance() throws Exception {
        // Initialize the database
        acceptanceRepository.saveAndFlush(acceptance);
        acceptanceSearchRepository.save(acceptance);

        // Search the acceptance
        restAcceptanceMockMvc.perform(get("/api/_search/acceptances?query=id:" + acceptance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acceptance.getId().intValue())))
            .andExpect(jsonPath("$.[*].criteria").value(hasItem(DEFAULT_CRITERIA.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Acceptance.class);
    }
}
