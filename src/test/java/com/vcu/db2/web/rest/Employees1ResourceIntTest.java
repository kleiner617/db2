package com.vcu.db2.web.rest;

import com.vcu.db2.Db2App;

import com.vcu.db2.domain.Employees1;
import com.vcu.db2.repository.Employees1Repository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the Employees1Resource REST controller.
 *
 * @see Employees1Resource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Db2App.class)
public class Employees1ResourceIntTest {

    private static final String DEFAULT_SSN = "AAAAAAAAA";
    private static final String UPDATED_SSN = "BBBBBBBBB";
    private static final String DEFAULT_CONTACT_NUMBER = "AAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBB";
    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";
    private static final String DEFAULT_SPECIALTY = "AAAAA";
    private static final String UPDATED_SPECIALTY = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    @Inject
    private Employees1Repository employees1Repository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEmployees1MockMvc;

    private Employees1 employees1;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Employees1Resource employees1Resource = new Employees1Resource();
        ReflectionTestUtils.setField(employees1Resource, "employees1Repository", employees1Repository);
        this.restEmployees1MockMvc = MockMvcBuilders.standaloneSetup(employees1Resource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employees1 createEntity(EntityManager em) {
        Employees1 employees1 = new Employees1()
                .ssn(DEFAULT_SSN)
                .contact_number(DEFAULT_CONTACT_NUMBER)
                .first_name(DEFAULT_FIRST_NAME)
                .last_name(DEFAULT_LAST_NAME)
                .specialty(DEFAULT_SPECIALTY)
                .address(DEFAULT_ADDRESS);
        return employees1;
    }

    @Before
    public void initTest() {
        employees1 = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployees1() throws Exception {
        int databaseSizeBeforeCreate = employees1Repository.findAll().size();

        // Create the Employees1

        restEmployees1MockMvc.perform(post("/api/employees-1-s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employees1)))
                .andExpect(status().isCreated());

        // Validate the Employees1 in the database
        List<Employees1> employees1S = employees1Repository.findAll();
        assertThat(employees1S).hasSize(databaseSizeBeforeCreate + 1);
        Employees1 testEmployees1 = employees1S.get(employees1S.size() - 1);
        assertThat(testEmployees1.getSsn()).isEqualTo(DEFAULT_SSN);
        assertThat(testEmployees1.getContact_number()).isEqualTo(DEFAULT_CONTACT_NUMBER);
        assertThat(testEmployees1.getFirst_name()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployees1.getLast_name()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployees1.getSpecialty()).isEqualTo(DEFAULT_SPECIALTY);
        assertThat(testEmployees1.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void checkSsnIsRequired() throws Exception {
        int databaseSizeBeforeTest = employees1Repository.findAll().size();
        // set the field null
        employees1.setSsn(null);

        // Create the Employees1, which fails.

        restEmployees1MockMvc.perform(post("/api/employees-1-s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employees1)))
                .andExpect(status().isBadRequest());

        List<Employees1> employees1S = employees1Repository.findAll();
        assertThat(employees1S).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmployees1S() throws Exception {
        // Initialize the database
        employees1Repository.saveAndFlush(employees1);

        // Get all the employees1S
        restEmployees1MockMvc.perform(get("/api/employees-1-s?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employees1.getId().intValue())))
                .andExpect(jsonPath("$.[*].ssn").value(hasItem(DEFAULT_SSN.toString())))
                .andExpect(jsonPath("$.[*].contact_number").value(hasItem(DEFAULT_CONTACT_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].first_name").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].last_name").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].specialty").value(hasItem(DEFAULT_SPECIALTY.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getEmployees1() throws Exception {
        // Initialize the database
        employees1Repository.saveAndFlush(employees1);

        // Get the employees1
        restEmployees1MockMvc.perform(get("/api/employees-1-s/{id}", employees1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employees1.getId().intValue()))
            .andExpect(jsonPath("$.ssn").value(DEFAULT_SSN.toString()))
            .andExpect(jsonPath("$.contact_number").value(DEFAULT_CONTACT_NUMBER.toString()))
            .andExpect(jsonPath("$.first_name").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.last_name").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.specialty").value(DEFAULT_SPECIALTY.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployees1() throws Exception {
        // Get the employees1
        restEmployees1MockMvc.perform(get("/api/employees-1-s/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployees1() throws Exception {
        // Initialize the database
        employees1Repository.saveAndFlush(employees1);
        int databaseSizeBeforeUpdate = employees1Repository.findAll().size();

        // Update the employees1
        Employees1 updatedEmployees1 = employees1Repository.findOne(employees1.getId());
        updatedEmployees1
                .ssn(UPDATED_SSN)
                .contact_number(UPDATED_CONTACT_NUMBER)
                .first_name(UPDATED_FIRST_NAME)
                .last_name(UPDATED_LAST_NAME)
                .specialty(UPDATED_SPECIALTY)
                .address(UPDATED_ADDRESS);

        restEmployees1MockMvc.perform(put("/api/employees-1-s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmployees1)))
                .andExpect(status().isOk());

        // Validate the Employees1 in the database
        List<Employees1> employees1S = employees1Repository.findAll();
        assertThat(employees1S).hasSize(databaseSizeBeforeUpdate);
        Employees1 testEmployees1 = employees1S.get(employees1S.size() - 1);
        assertThat(testEmployees1.getSsn()).isEqualTo(UPDATED_SSN);
        assertThat(testEmployees1.getContact_number()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testEmployees1.getFirst_name()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployees1.getLast_name()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployees1.getSpecialty()).isEqualTo(UPDATED_SPECIALTY);
        assertThat(testEmployees1.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void deleteEmployees1() throws Exception {
        // Initialize the database
        employees1Repository.saveAndFlush(employees1);
        int databaseSizeBeforeDelete = employees1Repository.findAll().size();

        // Get the employees1
        restEmployees1MockMvc.perform(delete("/api/employees-1-s/{id}", employees1.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Employees1> employees1S = employees1Repository.findAll();
        assertThat(employees1S).hasSize(databaseSizeBeforeDelete - 1);
    }
}
