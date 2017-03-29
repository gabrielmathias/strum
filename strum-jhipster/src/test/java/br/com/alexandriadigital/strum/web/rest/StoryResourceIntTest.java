package br.com.alexandriadigital.strum.web.rest;

import br.com.alexandriadigital.strum.StrumApp;

import br.com.alexandriadigital.strum.domain.Story;
import br.com.alexandriadigital.strum.domain.User;
import br.com.alexandriadigital.strum.domain.Sprint;
import br.com.alexandriadigital.strum.repository.StoryRepository;
import br.com.alexandriadigital.strum.service.StoryService;
import br.com.alexandriadigital.strum.repository.search.StorySearchRepository;
import br.com.alexandriadigital.strum.service.dto.StoryDTO;
import br.com.alexandriadigital.strum.service.mapper.StoryMapper;
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

import br.com.alexandriadigital.strum.domain.enumeration.StoryOrigin;
import br.com.alexandriadigital.strum.domain.enumeration.StoryStatus;
/**
 * Test class for the StoryResource REST controller.
 *
 * @see StoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StrumApp.class)
public class StoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final StoryOrigin DEFAULT_ORIGIN = StoryOrigin.SPRINT;
    private static final StoryOrigin UPDATED_ORIGIN = StoryOrigin.BUG;

    private static final StoryStatus DEFAULT_STATUS = StoryStatus.PLANNING;
    private static final StoryStatus UPDATED_STATUS = StoryStatus.READY;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private StoryMapper storyMapper;

    @Autowired
    private StoryService storyService;

    @Autowired
    private StorySearchRepository storySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStoryMockMvc;

    private Story story;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StoryResource storyResource = new StoryResource(storyService);
        this.restStoryMockMvc = MockMvcBuilders.standaloneSetup(storyResource)
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
    public static Story createEntity(EntityManager em) {
        Story story = new Story()
            .name(DEFAULT_NAME)
            .points(DEFAULT_POINTS)
            .description(DEFAULT_DESCRIPTION)
            .origin(DEFAULT_ORIGIN)
            .status(DEFAULT_STATUS);
        // Add required entity
        User created_by = UserResourceIntTest.createEntity(em);
        em.persist(created_by);
        em.flush();
        story.setCreated_by(created_by);
        // Add required entity
        Sprint sprint = SprintResourceIntTest.createEntity(em);
        em.persist(sprint);
        em.flush();
        story.setSprint(sprint);
        return story;
    }

    @Before
    public void initTest() {
        storySearchRepository.deleteAll();
        story = createEntity(em);
    }

    @Test
    @Transactional
    public void createStory() throws Exception {
        int databaseSizeBeforeCreate = storyRepository.findAll().size();

        // Create the Story
        StoryDTO storyDTO = storyMapper.storyToStoryDTO(story);
        restStoryMockMvc.perform(post("/api/stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storyDTO)))
            .andExpect(status().isCreated());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeCreate + 1);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStory.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testStory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStory.getOrigin()).isEqualTo(DEFAULT_ORIGIN);
        assertThat(testStory.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Story in Elasticsearch
        Story storyEs = storySearchRepository.findOne(testStory.getId());
        assertThat(storyEs).isEqualToComparingFieldByField(testStory);
    }

    @Test
    @Transactional
    public void createStoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storyRepository.findAll().size();

        // Create the Story with an existing ID
        story.setId(1L);
        StoryDTO storyDTO = storyMapper.storyToStoryDTO(story);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoryMockMvc.perform(post("/api/stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = storyRepository.findAll().size();
        // set the field null
        story.setName(null);

        // Create the Story, which fails.
        StoryDTO storyDTO = storyMapper.storyToStoryDTO(story);

        restStoryMockMvc.perform(post("/api/stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storyDTO)))
            .andExpect(status().isBadRequest());

        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOriginIsRequired() throws Exception {
        int databaseSizeBeforeTest = storyRepository.findAll().size();
        // set the field null
        story.setOrigin(null);

        // Create the Story, which fails.
        StoryDTO storyDTO = storyMapper.storyToStoryDTO(story);

        restStoryMockMvc.perform(post("/api/stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storyDTO)))
            .andExpect(status().isBadRequest());

        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = storyRepository.findAll().size();
        // set the field null
        story.setStatus(null);

        // Create the Story, which fails.
        StoryDTO storyDTO = storyMapper.storyToStoryDTO(story);

        restStoryMockMvc.perform(post("/api/stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storyDTO)))
            .andExpect(status().isBadRequest());

        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStories() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        // Get all the storyList
        restStoryMockMvc.perform(get("/api/stories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(story.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getStory() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        // Get the story
        restStoryMockMvc.perform(get("/api/stories/{id}", story.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(story.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStory() throws Exception {
        // Get the story
        restStoryMockMvc.perform(get("/api/stories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStory() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);
        storySearchRepository.save(story);
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();

        // Update the story
        Story updatedStory = storyRepository.findOne(story.getId());
        updatedStory
            .name(UPDATED_NAME)
            .points(UPDATED_POINTS)
            .description(UPDATED_DESCRIPTION)
            .origin(UPDATED_ORIGIN)
            .status(UPDATED_STATUS);
        StoryDTO storyDTO = storyMapper.storyToStoryDTO(updatedStory);

        restStoryMockMvc.perform(put("/api/stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storyDTO)))
            .andExpect(status().isOk());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStory.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testStory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStory.getOrigin()).isEqualTo(UPDATED_ORIGIN);
        assertThat(testStory.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Story in Elasticsearch
        Story storyEs = storySearchRepository.findOne(testStory.getId());
        assertThat(storyEs).isEqualToComparingFieldByField(testStory);
    }

    @Test
    @Transactional
    public void updateNonExistingStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();

        // Create the Story
        StoryDTO storyDTO = storyMapper.storyToStoryDTO(story);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStoryMockMvc.perform(put("/api/stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storyDTO)))
            .andExpect(status().isCreated());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStory() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);
        storySearchRepository.save(story);
        int databaseSizeBeforeDelete = storyRepository.findAll().size();

        // Get the story
        restStoryMockMvc.perform(delete("/api/stories/{id}", story.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean storyExistsInEs = storySearchRepository.exists(story.getId());
        assertThat(storyExistsInEs).isFalse();

        // Validate the database is empty
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStory() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);
        storySearchRepository.save(story);

        // Search the story
        restStoryMockMvc.perform(get("/api/_search/stories?query=id:" + story.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(story.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Story.class);
    }
}
