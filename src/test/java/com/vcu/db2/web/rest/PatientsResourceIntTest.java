package com.vcu.db2.web.rest;

import com.vcu.db2.Db2App;

import com.vcu.db2.domain.Patients;
import com.vcu.db2.repository.PatientsRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PatientsResource REST controller.
 *
 * @see PatientsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Db2App.class)
public class PatientsResourceIntTest {

    private static final String DEFAULT_SSN = "AAAAAAAAA";
    private static final String UPDATED_SSN = "BBBBBBBBB";
    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_GENDER = "AAAAA";
    private static final String UPDATED_GENDER = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    @Inject
    private PatientsRepository patientsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPatientsMockMvc;

    private Patients patients;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PatientsResource patientsResource = new PatientsResource();
        ReflectionTestUtils.setField(patientsResource, "patientsRepository", patientsRepository);
        this.restPatientsMockMvc = MockMvcBuilders.standaloneSetup(patientsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patients createEntity(EntityManager em) {
        Patients patients = new Patients()
                .ssn(DEFAULT_SSN)
                .first_name(DEFAULT_FIRST_NAME)
                .last_name(DEFAULT_LAST_NAME)
                .birthdate(DEFAULT_BIRTHDATE)
                .gender(DEFAULT_GENDER)
                .address(DEFAULT_ADDRESS);
        return patients;
    }

    @Before
    public void initTest() {
        patients = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatients() throws Exception {
        int databaseSizeBeforeCreate = patientsRepository.findAll().size();

        // Create the Patients

        restPatientsMockMvc.perform(post("/api/patients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(patients)))
                .andExpect(status().isCreated());

        // Validate the Patients in the database
        List<Patients> patients = patientsRepository.findAll();
        assertThat(patients).hasSize(databaseSizeBeforeCreate + 1);
        Patients testPatients = patients.get(patients.size() - 1);
        assertThat(testPatients.getSsn()).isEqualTo(DEFAULT_SSN);
        assertThat(testPatients.getFirst_name()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPatients.getLast_name()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPatients.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testPatients.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPatients.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void checkSsnIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientsRepository.findAll().size();
        // set the field null
        patients.setSsn(null);

        // Create the Patients, which fails.

        restPatientsMockMvc.perform(post("/api/patients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(patients)))
                .andExpect(status().isBadRequest());

        List<Patients> patients = patientsRepository.findAll();
        assertThat(patients).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientsRepository.findAll().size();
        // set the field null
        patients.setBirthdate(null);

        // Create the Patients, which fails.

        restPatientsMockMvc.perform(post("/api/patients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(patients)))
                .andExpect(status().isBadRequest());

        List<Patients> patients = patientsRepository.findAll();
        assertThat(patients).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPatients() throws Exception {
        // Initialize the database
        patientsRepository.saveAndFlush(patients);

        // Get all the patients
        restPatientsMockMvc.perform(get("/api/patients?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(patients.getId().intValue())))
                .andExpect(jsonPath("$.[*].ssn").value(hasItem(DEFAULT_SSN.toString())))
                .andExpect(jsonPath("$.[*].first_name").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].last_name").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
                .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getPatients() throws Exception {
        // Initialize the database
        patientsRepository.saveAndFlush(patients);

        // Get the patients
        restPatientsMockMvc.perform(get("/api/patients/{id}", patients.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(patients.getId().intValue()))
            .andExpect(jsonPath("$.ssn").value(DEFAULT_SSN.toString()))
            .andExpect(jsonPath("$.first_name").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.last_name").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPatients() throws Exception {
        // Get the patients
        restPatientsMockMvc.perform(get("/api/patients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatients() throws Exception {
        // Initialize the database
        patientsRepository.saveAndFlush(patients);
        int databaseSizeBeforeUpdate = patientsRepository.findAll().size();

        // Update the patients
        Patients updatedPatients = patientsRepository.findOne(patients.getId());
        updatedPatients
                .ssn(UPDATED_SSN)
                .first_name(UPDATED_FIRST_NAME)
                .last_name(UPDATED_LAST_NAME)
                .birthdate(UPDATED_BIRTHDATE)
                .gender(UPDATED_GENDER)
                .address(UPDATED_ADDRESS);

        restPatientsMockMvc.perform(put("/api/patients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPatients)))
                .andExpect(status().isOk());

        // Validate the Patients in the database
        List<Patients> patients = patientsRepository.findAll();
        assertThat(patients).hasSize(databaseSizeBeforeUpdate);
        Patients testPatients = patients.get(patients.size() - 1);
        assertThat(testPatients.getSsn()).isEqualTo(UPDATED_SSN);
        assertThat(testPatients.getFirst_name()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPatients.getLast_name()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPatients.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testPatients.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPatients.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void deletePatients() throws Exception {
        // Initialize the database
        patientsRepository.saveAndFlush(patients);
        int databaseSizeBeforeDelete = patientsRepository.findAll().size();

        // Get the patients
        restPatientsMockMvc.perform(delete("/api/patients/{id}", patients.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Patients> patients = patientsRepository.findAll();
        assertThat(patients).hasSize(databaseSizeBeforeDelete - 1);
    }
}
