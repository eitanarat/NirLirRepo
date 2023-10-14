package com.nirlir.web.rest;

import static com.nirlir.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nirlir.IntegrationTest;
import com.nirlir.domain.ServiceRequest;
import com.nirlir.domain.Technician;
import com.nirlir.repository.TechnicianRepository;
import com.nirlir.service.criteria.TechnicianCriteria;
import com.nirlir.service.dto.TechnicianDTO;
import com.nirlir.service.mapper.TechnicianMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TechnicianResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TechnicianResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LANG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_LANG_KEY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_BIRTHDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_BIRTHDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_BIRTHDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/technicians";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TechnicianRepository technicianRepository;

    @Autowired
    private TechnicianMapper technicianMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTechnicianMockMvc;

    private Technician technician;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Technician createEntity(EntityManager em) {
        Technician technician = new Technician()
            .userId(DEFAULT_USER_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .langKey(DEFAULT_LANG_KEY)
            .birthdate(DEFAULT_BIRTHDATE);
        return technician;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Technician createUpdatedEntity(EntityManager em) {
        Technician technician = new Technician()
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .langKey(UPDATED_LANG_KEY)
            .birthdate(UPDATED_BIRTHDATE);
        return technician;
    }

    @BeforeEach
    public void initTest() {
        technician = createEntity(em);
    }

    @Test
    @Transactional
    void createTechnician() throws Exception {
        int databaseSizeBeforeCreate = technicianRepository.findAll().size();
        // Create the Technician
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);
        restTechnicianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(technicianDTO)))
            .andExpect(status().isCreated());

        // Validate the Technician in the database
        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeCreate + 1);
        Technician testTechnician = technicianList.get(technicianList.size() - 1);
        assertThat(testTechnician.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testTechnician.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTechnician.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTechnician.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTechnician.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testTechnician.getLangKey()).isEqualTo(DEFAULT_LANG_KEY);
        assertThat(testTechnician.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
    }

    @Test
    @Transactional
    void createTechnicianWithExistingId() throws Exception {
        // Create the Technician with an existing ID
        technician.setId(1L);
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        int databaseSizeBeforeCreate = technicianRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechnicianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(technicianDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Technician in the database
        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = technicianRepository.findAll().size();
        // set the field null
        technician.setFirstName(null);

        // Create the Technician, which fails.
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        restTechnicianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(technicianDTO)))
            .andExpect(status().isBadRequest());

        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = technicianRepository.findAll().size();
        // set the field null
        technician.setLastName(null);

        // Create the Technician, which fails.
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        restTechnicianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(technicianDTO)))
            .andExpect(status().isBadRequest());

        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = technicianRepository.findAll().size();
        // set the field null
        technician.setEmail(null);

        // Create the Technician, which fails.
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        restTechnicianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(technicianDTO)))
            .andExpect(status().isBadRequest());

        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = technicianRepository.findAll().size();
        // set the field null
        technician.setMobileNumber(null);

        // Create the Technician, which fails.
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        restTechnicianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(technicianDTO)))
            .andExpect(status().isBadRequest());

        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLangKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = technicianRepository.findAll().size();
        // set the field null
        technician.setLangKey(null);

        // Create the Technician, which fails.
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        restTechnicianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(technicianDTO)))
            .andExpect(status().isBadRequest());

        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = technicianRepository.findAll().size();
        // set the field null
        technician.setBirthdate(null);

        // Create the Technician, which fails.
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        restTechnicianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(technicianDTO)))
            .andExpect(status().isBadRequest());

        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTechnicians() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList
        restTechnicianMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(technician.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANG_KEY)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(sameInstant(DEFAULT_BIRTHDATE))));
    }

    @Test
    @Transactional
    void getTechnician() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get the technician
        restTechnicianMockMvc
            .perform(get(ENTITY_API_URL_ID, technician.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(technician.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER))
            .andExpect(jsonPath("$.langKey").value(DEFAULT_LANG_KEY))
            .andExpect(jsonPath("$.birthdate").value(sameInstant(DEFAULT_BIRTHDATE)));
    }

    @Test
    @Transactional
    void getTechniciansByIdFiltering() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        Long id = technician.getId();

        defaultTechnicianShouldBeFound("id.equals=" + id);
        defaultTechnicianShouldNotBeFound("id.notEquals=" + id);

        defaultTechnicianShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTechnicianShouldNotBeFound("id.greaterThan=" + id);

        defaultTechnicianShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTechnicianShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTechniciansByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where userId equals to DEFAULT_USER_ID
        defaultTechnicianShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the technicianList where userId equals to UPDATED_USER_ID
        defaultTechnicianShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTechniciansByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultTechnicianShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the technicianList where userId equals to UPDATED_USER_ID
        defaultTechnicianShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTechniciansByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where userId is not null
        defaultTechnicianShouldBeFound("userId.specified=true");

        // Get all the technicianList where userId is null
        defaultTechnicianShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllTechniciansByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where userId is greater than or equal to DEFAULT_USER_ID
        defaultTechnicianShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the technicianList where userId is greater than or equal to UPDATED_USER_ID
        defaultTechnicianShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTechniciansByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where userId is less than or equal to DEFAULT_USER_ID
        defaultTechnicianShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the technicianList where userId is less than or equal to SMALLER_USER_ID
        defaultTechnicianShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllTechniciansByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where userId is less than DEFAULT_USER_ID
        defaultTechnicianShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the technicianList where userId is less than UPDATED_USER_ID
        defaultTechnicianShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTechniciansByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where userId is greater than DEFAULT_USER_ID
        defaultTechnicianShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the technicianList where userId is greater than SMALLER_USER_ID
        defaultTechnicianShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllTechniciansByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where firstName equals to DEFAULT_FIRST_NAME
        defaultTechnicianShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the technicianList where firstName equals to UPDATED_FIRST_NAME
        defaultTechnicianShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTechniciansByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultTechnicianShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the technicianList where firstName equals to UPDATED_FIRST_NAME
        defaultTechnicianShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTechniciansByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where firstName is not null
        defaultTechnicianShouldBeFound("firstName.specified=true");

        // Get all the technicianList where firstName is null
        defaultTechnicianShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllTechniciansByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where firstName contains DEFAULT_FIRST_NAME
        defaultTechnicianShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the technicianList where firstName contains UPDATED_FIRST_NAME
        defaultTechnicianShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTechniciansByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where firstName does not contain DEFAULT_FIRST_NAME
        defaultTechnicianShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the technicianList where firstName does not contain UPDATED_FIRST_NAME
        defaultTechnicianShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTechniciansByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where lastName equals to DEFAULT_LAST_NAME
        defaultTechnicianShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the technicianList where lastName equals to UPDATED_LAST_NAME
        defaultTechnicianShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTechniciansByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultTechnicianShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the technicianList where lastName equals to UPDATED_LAST_NAME
        defaultTechnicianShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTechniciansByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where lastName is not null
        defaultTechnicianShouldBeFound("lastName.specified=true");

        // Get all the technicianList where lastName is null
        defaultTechnicianShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllTechniciansByLastNameContainsSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where lastName contains DEFAULT_LAST_NAME
        defaultTechnicianShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the technicianList where lastName contains UPDATED_LAST_NAME
        defaultTechnicianShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTechniciansByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where lastName does not contain DEFAULT_LAST_NAME
        defaultTechnicianShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the technicianList where lastName does not contain UPDATED_LAST_NAME
        defaultTechnicianShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTechniciansByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where email equals to DEFAULT_EMAIL
        defaultTechnicianShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the technicianList where email equals to UPDATED_EMAIL
        defaultTechnicianShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTechniciansByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTechnicianShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the technicianList where email equals to UPDATED_EMAIL
        defaultTechnicianShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTechniciansByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where email is not null
        defaultTechnicianShouldBeFound("email.specified=true");

        // Get all the technicianList where email is null
        defaultTechnicianShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllTechniciansByEmailContainsSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where email contains DEFAULT_EMAIL
        defaultTechnicianShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the technicianList where email contains UPDATED_EMAIL
        defaultTechnicianShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTechniciansByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where email does not contain DEFAULT_EMAIL
        defaultTechnicianShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the technicianList where email does not contain UPDATED_EMAIL
        defaultTechnicianShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTechniciansByMobileNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where mobileNumber equals to DEFAULT_MOBILE_NUMBER
        defaultTechnicianShouldBeFound("mobileNumber.equals=" + DEFAULT_MOBILE_NUMBER);

        // Get all the technicianList where mobileNumber equals to UPDATED_MOBILE_NUMBER
        defaultTechnicianShouldNotBeFound("mobileNumber.equals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTechniciansByMobileNumberIsInShouldWork() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where mobileNumber in DEFAULT_MOBILE_NUMBER or UPDATED_MOBILE_NUMBER
        defaultTechnicianShouldBeFound("mobileNumber.in=" + DEFAULT_MOBILE_NUMBER + "," + UPDATED_MOBILE_NUMBER);

        // Get all the technicianList where mobileNumber equals to UPDATED_MOBILE_NUMBER
        defaultTechnicianShouldNotBeFound("mobileNumber.in=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTechniciansByMobileNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where mobileNumber is not null
        defaultTechnicianShouldBeFound("mobileNumber.specified=true");

        // Get all the technicianList where mobileNumber is null
        defaultTechnicianShouldNotBeFound("mobileNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTechniciansByMobileNumberContainsSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where mobileNumber contains DEFAULT_MOBILE_NUMBER
        defaultTechnicianShouldBeFound("mobileNumber.contains=" + DEFAULT_MOBILE_NUMBER);

        // Get all the technicianList where mobileNumber contains UPDATED_MOBILE_NUMBER
        defaultTechnicianShouldNotBeFound("mobileNumber.contains=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTechniciansByMobileNumberNotContainsSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where mobileNumber does not contain DEFAULT_MOBILE_NUMBER
        defaultTechnicianShouldNotBeFound("mobileNumber.doesNotContain=" + DEFAULT_MOBILE_NUMBER);

        // Get all the technicianList where mobileNumber does not contain UPDATED_MOBILE_NUMBER
        defaultTechnicianShouldBeFound("mobileNumber.doesNotContain=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTechniciansByLangKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where langKey equals to DEFAULT_LANG_KEY
        defaultTechnicianShouldBeFound("langKey.equals=" + DEFAULT_LANG_KEY);

        // Get all the technicianList where langKey equals to UPDATED_LANG_KEY
        defaultTechnicianShouldNotBeFound("langKey.equals=" + UPDATED_LANG_KEY);
    }

    @Test
    @Transactional
    void getAllTechniciansByLangKeyIsInShouldWork() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where langKey in DEFAULT_LANG_KEY or UPDATED_LANG_KEY
        defaultTechnicianShouldBeFound("langKey.in=" + DEFAULT_LANG_KEY + "," + UPDATED_LANG_KEY);

        // Get all the technicianList where langKey equals to UPDATED_LANG_KEY
        defaultTechnicianShouldNotBeFound("langKey.in=" + UPDATED_LANG_KEY);
    }

    @Test
    @Transactional
    void getAllTechniciansByLangKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where langKey is not null
        defaultTechnicianShouldBeFound("langKey.specified=true");

        // Get all the technicianList where langKey is null
        defaultTechnicianShouldNotBeFound("langKey.specified=false");
    }

    @Test
    @Transactional
    void getAllTechniciansByLangKeyContainsSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where langKey contains DEFAULT_LANG_KEY
        defaultTechnicianShouldBeFound("langKey.contains=" + DEFAULT_LANG_KEY);

        // Get all the technicianList where langKey contains UPDATED_LANG_KEY
        defaultTechnicianShouldNotBeFound("langKey.contains=" + UPDATED_LANG_KEY);
    }

    @Test
    @Transactional
    void getAllTechniciansByLangKeyNotContainsSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where langKey does not contain DEFAULT_LANG_KEY
        defaultTechnicianShouldNotBeFound("langKey.doesNotContain=" + DEFAULT_LANG_KEY);

        // Get all the technicianList where langKey does not contain UPDATED_LANG_KEY
        defaultTechnicianShouldBeFound("langKey.doesNotContain=" + UPDATED_LANG_KEY);
    }

    @Test
    @Transactional
    void getAllTechniciansByBirthdateIsEqualToSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where birthdate equals to DEFAULT_BIRTHDATE
        defaultTechnicianShouldBeFound("birthdate.equals=" + DEFAULT_BIRTHDATE);

        // Get all the technicianList where birthdate equals to UPDATED_BIRTHDATE
        defaultTechnicianShouldNotBeFound("birthdate.equals=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllTechniciansByBirthdateIsInShouldWork() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where birthdate in DEFAULT_BIRTHDATE or UPDATED_BIRTHDATE
        defaultTechnicianShouldBeFound("birthdate.in=" + DEFAULT_BIRTHDATE + "," + UPDATED_BIRTHDATE);

        // Get all the technicianList where birthdate equals to UPDATED_BIRTHDATE
        defaultTechnicianShouldNotBeFound("birthdate.in=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllTechniciansByBirthdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where birthdate is not null
        defaultTechnicianShouldBeFound("birthdate.specified=true");

        // Get all the technicianList where birthdate is null
        defaultTechnicianShouldNotBeFound("birthdate.specified=false");
    }

    @Test
    @Transactional
    void getAllTechniciansByBirthdateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where birthdate is greater than or equal to DEFAULT_BIRTHDATE
        defaultTechnicianShouldBeFound("birthdate.greaterThanOrEqual=" + DEFAULT_BIRTHDATE);

        // Get all the technicianList where birthdate is greater than or equal to UPDATED_BIRTHDATE
        defaultTechnicianShouldNotBeFound("birthdate.greaterThanOrEqual=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllTechniciansByBirthdateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where birthdate is less than or equal to DEFAULT_BIRTHDATE
        defaultTechnicianShouldBeFound("birthdate.lessThanOrEqual=" + DEFAULT_BIRTHDATE);

        // Get all the technicianList where birthdate is less than or equal to SMALLER_BIRTHDATE
        defaultTechnicianShouldNotBeFound("birthdate.lessThanOrEqual=" + SMALLER_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllTechniciansByBirthdateIsLessThanSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where birthdate is less than DEFAULT_BIRTHDATE
        defaultTechnicianShouldNotBeFound("birthdate.lessThan=" + DEFAULT_BIRTHDATE);

        // Get all the technicianList where birthdate is less than UPDATED_BIRTHDATE
        defaultTechnicianShouldBeFound("birthdate.lessThan=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllTechniciansByBirthdateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        // Get all the technicianList where birthdate is greater than DEFAULT_BIRTHDATE
        defaultTechnicianShouldNotBeFound("birthdate.greaterThan=" + DEFAULT_BIRTHDATE);

        // Get all the technicianList where birthdate is greater than SMALLER_BIRTHDATE
        defaultTechnicianShouldBeFound("birthdate.greaterThan=" + SMALLER_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllTechniciansByServiceRequestIsEqualToSomething() throws Exception {
        ServiceRequest serviceRequest;
        if (TestUtil.findAll(em, ServiceRequest.class).isEmpty()) {
            technicianRepository.saveAndFlush(technician);
            serviceRequest = ServiceRequestResourceIT.createEntity(em);
        } else {
            serviceRequest = TestUtil.findAll(em, ServiceRequest.class).get(0);
        }
        em.persist(serviceRequest);
        em.flush();
        technician.addServiceRequest(serviceRequest);
        technicianRepository.saveAndFlush(technician);
        Long serviceRequestId = serviceRequest.getId();

        // Get all the technicianList where serviceRequest equals to serviceRequestId
        defaultTechnicianShouldBeFound("serviceRequestId.equals=" + serviceRequestId);

        // Get all the technicianList where serviceRequest equals to (serviceRequestId + 1)
        defaultTechnicianShouldNotBeFound("serviceRequestId.equals=" + (serviceRequestId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTechnicianShouldBeFound(String filter) throws Exception {
        restTechnicianMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(technician.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANG_KEY)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(sameInstant(DEFAULT_BIRTHDATE))));

        // Check, that the count call also returns 1
        restTechnicianMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTechnicianShouldNotBeFound(String filter) throws Exception {
        restTechnicianMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTechnicianMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTechnician() throws Exception {
        // Get the technician
        restTechnicianMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTechnician() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        int databaseSizeBeforeUpdate = technicianRepository.findAll().size();

        // Update the technician
        Technician updatedTechnician = technicianRepository.findById(technician.getId()).get();
        // Disconnect from session so that the updates on updatedTechnician are not directly saved in db
        em.detach(updatedTechnician);
        updatedTechnician
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .langKey(UPDATED_LANG_KEY)
            .birthdate(UPDATED_BIRTHDATE);
        TechnicianDTO technicianDTO = technicianMapper.toDto(updatedTechnician);

        restTechnicianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, technicianDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(technicianDTO))
            )
            .andExpect(status().isOk());

        // Validate the Technician in the database
        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeUpdate);
        Technician testTechnician = technicianList.get(technicianList.size() - 1);
        assertThat(testTechnician.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testTechnician.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTechnician.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTechnician.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTechnician.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testTechnician.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testTechnician.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void putNonExistingTechnician() throws Exception {
        int databaseSizeBeforeUpdate = technicianRepository.findAll().size();
        technician.setId(count.incrementAndGet());

        // Create the Technician
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechnicianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, technicianDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(technicianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Technician in the database
        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTechnician() throws Exception {
        int databaseSizeBeforeUpdate = technicianRepository.findAll().size();
        technician.setId(count.incrementAndGet());

        // Create the Technician
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechnicianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(technicianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Technician in the database
        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTechnician() throws Exception {
        int databaseSizeBeforeUpdate = technicianRepository.findAll().size();
        technician.setId(count.incrementAndGet());

        // Create the Technician
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechnicianMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(technicianDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Technician in the database
        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTechnicianWithPatch() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        int databaseSizeBeforeUpdate = technicianRepository.findAll().size();

        // Update the technician using partial update
        Technician partialUpdatedTechnician = new Technician();
        partialUpdatedTechnician.setId(technician.getId());

        partialUpdatedTechnician.userId(UPDATED_USER_ID).mobileNumber(UPDATED_MOBILE_NUMBER).langKey(UPDATED_LANG_KEY);

        restTechnicianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechnician.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTechnician))
            )
            .andExpect(status().isOk());

        // Validate the Technician in the database
        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeUpdate);
        Technician testTechnician = technicianList.get(technicianList.size() - 1);
        assertThat(testTechnician.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testTechnician.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTechnician.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTechnician.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTechnician.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testTechnician.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testTechnician.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
    }

    @Test
    @Transactional
    void fullUpdateTechnicianWithPatch() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        int databaseSizeBeforeUpdate = technicianRepository.findAll().size();

        // Update the technician using partial update
        Technician partialUpdatedTechnician = new Technician();
        partialUpdatedTechnician.setId(technician.getId());

        partialUpdatedTechnician
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .langKey(UPDATED_LANG_KEY)
            .birthdate(UPDATED_BIRTHDATE);

        restTechnicianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechnician.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTechnician))
            )
            .andExpect(status().isOk());

        // Validate the Technician in the database
        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeUpdate);
        Technician testTechnician = technicianList.get(technicianList.size() - 1);
        assertThat(testTechnician.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testTechnician.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTechnician.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTechnician.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTechnician.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testTechnician.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testTechnician.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void patchNonExistingTechnician() throws Exception {
        int databaseSizeBeforeUpdate = technicianRepository.findAll().size();
        technician.setId(count.incrementAndGet());

        // Create the Technician
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechnicianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, technicianDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(technicianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Technician in the database
        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTechnician() throws Exception {
        int databaseSizeBeforeUpdate = technicianRepository.findAll().size();
        technician.setId(count.incrementAndGet());

        // Create the Technician
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechnicianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(technicianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Technician in the database
        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTechnician() throws Exception {
        int databaseSizeBeforeUpdate = technicianRepository.findAll().size();
        technician.setId(count.incrementAndGet());

        // Create the Technician
        TechnicianDTO technicianDTO = technicianMapper.toDto(technician);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechnicianMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(technicianDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Technician in the database
        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTechnician() throws Exception {
        // Initialize the database
        technicianRepository.saveAndFlush(technician);

        int databaseSizeBeforeDelete = technicianRepository.findAll().size();

        // Delete the technician
        restTechnicianMockMvc
            .perform(delete(ENTITY_API_URL_ID, technician.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Technician> technicianList = technicianRepository.findAll();
        assertThat(technicianList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
