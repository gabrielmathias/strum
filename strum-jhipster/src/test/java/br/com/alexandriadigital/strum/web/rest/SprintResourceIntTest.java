package br.com.alexandriadigital.strum.web.rest;

import br.com.alexandriadigital.strum.StrumApp;

import br.com.alexandriadigital.strum.domain.Sprint;
import br.com.alexandriadigital.strum.domain.Strum;
import br.com.alexandriadigital.strum.domain.User;
import br.com.alexandriadigital.strum.repository.SprintRepository;
import br.com.alexandriadigital.strum.service.SprintService;
import br.com.alexandriadigital.strum.repository.search.SprintSearchRepository;
import br.com.alexandriadigital.strum.service.dto.SprintDTO;
import br.com.alexandriadigital.strum.service.mapper.SprintMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static br.com.alexandriadigital.strum.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.alexandriadigital.strum.domain.enumeration.SprintStatus;
/**
 * Test class for the SprintResource REST controller.
 *
 * @see SprintResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StrumApp.class)
public class SprintResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_DAYS_PLANNED = 1;
    private static final Integer UPDATED_DAYS_PLANNED = 2;

    private static final Integer DEFAULT_DAYS_USED = 1;
    private static final Integer UPDATED_DAYS_USED = 2;

    private static final SprintStatus DEFAULT_STATUS = SprintStatus.READY;
    private static final SprintStatus UPDATED_STATUS = SprintStatus.RUNNING;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private SprintMapper sprintMapper;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private SprintSearchRepository sprintSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSprintMockMvc;

    private Sprint sprint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SprintResource sprintResource = new SprintResource(sprintService);
        this.restSprintMockMvc = MockMvcBuilders.standaloneSetup(sprintResource)
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
    public static Sprint createEntity(EntityManager em) {
        Sprint sprint = new Sprint()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .start_at(DEFAULT_START_AT)
            .end_at(DEFAULT_END_AT)
            .days_planned(DEFAULT_DAYS_PLANNED)
            .days_used(DEFAULT_DAYS_USED)
            .status(DEFAULT_STATUS);
        // Add required entity
        Strum strum = StrumResourceIntTest.createEntity(em);
        em.persist(strum);
        em.flush();
        sprint.setStrum(strum);
        // Add required entity
        User created_by = UserResourceIntTest.createEntity(em);
        em.persist(created_by);
        em.flush();
        sprint.setCreated_by(created_by);
        return sprint;
    }

    @Before
    public void initTest() {
        sprintSearchRepository.deleteAll();
        sprint = createEntity(em);
    }

    @Test
    @Transactional
    public void createSprint() throws Exception {
        int databaseSizeBeforeCreate = sprintRepository.findAll().size();

        // Create the Sprint
        SprintDTO sprintDTO = sprintMapper.sprintToSprintDTO(sprint);
        restSprintMockMvc.perform(post("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isCreated());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeCreate + 1);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSprint.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSprint.getStart_at()).isEqualTo(DEFAULT_START_AT);
        assertThat(testSprint.getEnd_at()).isEqualTo(DEFAULT_END_AT);
        assertThat(testSprint.getDays_planned()).isEqualTo(DEFAULT_DAYS_PLANNED);
        assertThat(testSprint.getDays_used()).isEqualTo(DEFAULT_DAYS_USED);
        assertThat(testSprint.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Sprint in Elasticsearch
        Sprint sprintEs = sprintSearchRepository.findOne(testSprint.getId());
        assertThat(sprintEs).isEqualToComparingFieldByField(testSprint);
    }

    @Test
    @Transactional
    public void createSprintWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sprintRepository.findAll().size();

        // Create the Sprint with an existing ID
        sprint.setId(1L);
        SprintDTO sprintDTO = sprintMapper.sprintToSprintDTO(sprint);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSprintMockMvc.perform(post("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprintRepository.findAll().size();
        // set the field null
        sprint.setName(null);

        // Create the Sprint, which fails.
        SprintDTO sprintDTO = sprintMapper.sprintToSprintDTO(sprint);

        restSprintMockMvc.perform(post("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isBadRequest());

        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprintRepository.findAll().size();
        // set the field null
        sprint.setStatus(null);

        // Create the Sprint, which fails.
        SprintDTO sprintDTO = sprintMapper.sprintToSprintDTO(sprint);

        restSprintMockMvc.perform(post("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isBadRequest());

        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSprints() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList
        restSprintMockMvc.perform(get("/api/sprints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].start_at").value(hasItem(sameInstant(DEFAULT_START_AT))))
            .andExpect(jsonPath("$.[*].end_at").value(hasItem(sameInstant(DEFAULT_END_AT))))
            .andExpect(jsonPath("$.[*].days_planned").value(hasItem(DEFAULT_DAYS_PLANNED)))
            .andExpect(jsonPath("$.[*].days_used").value(hasItem(DEFAULT_DAYS_USED)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get the sprint
        restSprintMockMvc.perform(get("/api/sprints/{id}", sprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sprint.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.start_at").value(sameInstant(DEFAULT_START_AT)))
            .andExpect(jsonPath("$.end_at").value(sameInstant(DEFAULT_END_AT)))
            .andExpect(jsonPath("$.days_planned").value(DEFAULT_DAYS_PLANNED))
            .andExpect(jsonPath("$.days_used").value(DEFAULT_DAYS_USED))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSprint() throws Exception {
        // Get the sprint
        restSprintMockMvc.perform(get("/api/sprints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);
        sprintSearchRepository.save(sprint);
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Update the sprint
        Sprint updatedSprint = sprintRepository.findOne(sprint.getId());
        updatedSprint
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .start_at(UPDATED_START_AT)
            .end_at(UPDATED_END_AT)
            .days_planned(UPDATED_DAYS_PLANNED)
            .days_used(UPDATED_DAYS_USED)
            .status(UPDATED_STATUS);
        SprintDTO sprintDTO = sprintMapper.sprintToSprintDTO(updatedSprint);

        restSprintMockMvc.perform(put("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isOk());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSprint.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSprint.getStart_at()).isEqualTo(UPDATED_START_AT);
        assertThat(testSprint.getEnd_at()).isEqualTo(UPDATED_END_AT);
        assertThat(testSprint.getDays_planned()).isEqualTo(UPDATED_DAYS_PLANNED);
        assertThat(testSprint.getDays_used()).isEqualTo(UPDATED_DAYS_USED);
        assertThat(testSprint.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Sprint in Elasticsearch
        Sprint sprintEs = sprintSearchRepository.findOne(testSprint.getId());
        assertThat(sprintEs).isEqualToComparingFieldByField(testSprint);
    }

    @Test
    @Transactional
    public void updateNonExistingSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Create the Sprint
        SprintDTO sprintDTO = sprintMapper.sprintToSprintDTO(sprint);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSprintMockMvc.perform(put("/api/sprints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isCreated());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);
        sprintSearchRepository.save(sprint);
        int databaseSizeBeforeDelete = sprintRepository.findAll().size();

        // Get the sprint
        restSprintMockMvc.perform(delete("/api/sprints/{id}", sprint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sprintExistsInEs = sprintSearchRepository.exists(sprint.getId());
        assertThat(sprintExistsInEs).isFalse();

        // Validate the database is empty
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);
        sprintSearchRepository.save(sprint);

        // Search the sprint
        restSprintMockMvc.perform(get("/api/_search/sprints?query=id:" + sprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].start_at").value(hasItem(sameInstant(DEFAULT_START_AT))))
            .andExpect(jsonPath("$.[*].end_at").value(hasItem(sameInstant(DEFAULT_END_AT))))
            .andExpect(jsonPath("$.[*].days_planned").value(hasItem(DEFAULT_DAYS_PLANNED)))
            .andExpect(jsonPath("$.[*].days_used").value(hasItem(DEFAULT_DAYS_USED)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sprint.class);
    }
}
