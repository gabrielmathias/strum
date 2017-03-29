package br.com.alexandriadigital.strum.web.rest;

import br.com.alexandriadigital.strum.StrumApp;

import br.com.alexandriadigital.strum.domain.Strum;
import br.com.alexandriadigital.strum.domain.User;
import br.com.alexandriadigital.strum.repository.StrumRepository;
import br.com.alexandriadigital.strum.service.StrumService;
import br.com.alexandriadigital.strum.repository.search.StrumSearchRepository;
import br.com.alexandriadigital.strum.service.dto.StrumDTO;
import br.com.alexandriadigital.strum.service.mapper.StrumMapper;
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

import br.com.alexandriadigital.strum.domain.enumeration.StrumStatus;
/**
 * Test class for the StrumResource REST controller.
 *
 * @see StrumResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StrumApp.class)
public class StrumResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final StrumStatus DEFAULT_STATUS = StrumStatus.READY;
    private static final StrumStatus UPDATED_STATUS = StrumStatus.ARCHIVED;

    @Autowired
    private StrumRepository strumRepository;

    @Autowired
    private StrumMapper strumMapper;

    @Autowired
    private StrumService strumService;

    @Autowired
    private StrumSearchRepository strumSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStrumMockMvc;

    private Strum strum;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StrumResource strumResource = new StrumResource(strumService);
        this.restStrumMockMvc = MockMvcBuilders.standaloneSetup(strumResource)
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
    public static Strum createEntity(EntityManager em) {
        Strum strum = new Strum()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS);
        // Add required entity
        User owner = UserResourceIntTest.createEntity(em);
        em.persist(owner);
        em.flush();
        strum.setOwner(owner);
        return strum;
    }

    @Before
    public void initTest() {
        strumSearchRepository.deleteAll();
        strum = createEntity(em);
    }

    @Test
    @Transactional
    public void createStrum() throws Exception {
        int databaseSizeBeforeCreate = strumRepository.findAll().size();

        // Create the Strum
        StrumDTO strumDTO = strumMapper.strumToStrumDTO(strum);
        restStrumMockMvc.perform(post("/api/strums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(strumDTO)))
            .andExpect(status().isCreated());

        // Validate the Strum in the database
        List<Strum> strumList = strumRepository.findAll();
        assertThat(strumList).hasSize(databaseSizeBeforeCreate + 1);
        Strum testStrum = strumList.get(strumList.size() - 1);
        assertThat(testStrum.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStrum.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStrum.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Strum in Elasticsearch
        Strum strumEs = strumSearchRepository.findOne(testStrum.getId());
        assertThat(strumEs).isEqualToComparingFieldByField(testStrum);
    }

    @Test
    @Transactional
    public void createStrumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = strumRepository.findAll().size();

        // Create the Strum with an existing ID
        strum.setId(1L);
        StrumDTO strumDTO = strumMapper.strumToStrumDTO(strum);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStrumMockMvc.perform(post("/api/strums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(strumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Strum> strumList = strumRepository.findAll();
        assertThat(strumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = strumRepository.findAll().size();
        // set the field null
        strum.setName(null);

        // Create the Strum, which fails.
        StrumDTO strumDTO = strumMapper.strumToStrumDTO(strum);

        restStrumMockMvc.perform(post("/api/strums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(strumDTO)))
            .andExpect(status().isBadRequest());

        List<Strum> strumList = strumRepository.findAll();
        assertThat(strumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = strumRepository.findAll().size();
        // set the field null
        strum.setStatus(null);

        // Create the Strum, which fails.
        StrumDTO strumDTO = strumMapper.strumToStrumDTO(strum);

        restStrumMockMvc.perform(post("/api/strums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(strumDTO)))
            .andExpect(status().isBadRequest());

        List<Strum> strumList = strumRepository.findAll();
        assertThat(strumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStrums() throws Exception {
        // Initialize the database
        strumRepository.saveAndFlush(strum);

        // Get all the strumList
        restStrumMockMvc.perform(get("/api/strums?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(strum.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getStrum() throws Exception {
        // Initialize the database
        strumRepository.saveAndFlush(strum);

        // Get the strum
        restStrumMockMvc.perform(get("/api/strums/{id}", strum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(strum.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStrum() throws Exception {
        // Get the strum
        restStrumMockMvc.perform(get("/api/strums/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStrum() throws Exception {
        // Initialize the database
        strumRepository.saveAndFlush(strum);
        strumSearchRepository.save(strum);
        int databaseSizeBeforeUpdate = strumRepository.findAll().size();

        // Update the strum
        Strum updatedStrum = strumRepository.findOne(strum.getId());
        updatedStrum
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS);
        StrumDTO strumDTO = strumMapper.strumToStrumDTO(updatedStrum);

        restStrumMockMvc.perform(put("/api/strums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(strumDTO)))
            .andExpect(status().isOk());

        // Validate the Strum in the database
        List<Strum> strumList = strumRepository.findAll();
        assertThat(strumList).hasSize(databaseSizeBeforeUpdate);
        Strum testStrum = strumList.get(strumList.size() - 1);
        assertThat(testStrum.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStrum.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStrum.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Strum in Elasticsearch
        Strum strumEs = strumSearchRepository.findOne(testStrum.getId());
        assertThat(strumEs).isEqualToComparingFieldByField(testStrum);
    }

    @Test
    @Transactional
    public void updateNonExistingStrum() throws Exception {
        int databaseSizeBeforeUpdate = strumRepository.findAll().size();

        // Create the Strum
        StrumDTO strumDTO = strumMapper.strumToStrumDTO(strum);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStrumMockMvc.perform(put("/api/strums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(strumDTO)))
            .andExpect(status().isCreated());

        // Validate the Strum in the database
        List<Strum> strumList = strumRepository.findAll();
        assertThat(strumList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStrum() throws Exception {
        // Initialize the database
        strumRepository.saveAndFlush(strum);
        strumSearchRepository.save(strum);
        int databaseSizeBeforeDelete = strumRepository.findAll().size();

        // Get the strum
        restStrumMockMvc.perform(delete("/api/strums/{id}", strum.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean strumExistsInEs = strumSearchRepository.exists(strum.getId());
        assertThat(strumExistsInEs).isFalse();

        // Validate the database is empty
        List<Strum> strumList = strumRepository.findAll();
        assertThat(strumList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStrum() throws Exception {
        // Initialize the database
        strumRepository.saveAndFlush(strum);
        strumSearchRepository.save(strum);

        // Search the strum
        restStrumMockMvc.perform(get("/api/_search/strums?query=id:" + strum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(strum.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Strum.class);
    }
}
