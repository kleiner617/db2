package com.vcu.db2.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vcu.db2.domain.Patients;

import com.vcu.db2.repository.PatientsRepository;
import com.vcu.db2.web.rest.util.HeaderUtil;
import com.vcu.db2.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Patients.
 */
@RestController
@RequestMapping("/api")
public class PatientsResource {

    private final Logger log = LoggerFactory.getLogger(PatientsResource.class);
        
    @Inject
    private PatientsRepository patientsRepository;

    /**
     * POST  /patients : Create a new patients.
     *
     * @param patients the patients to create
     * @return the ResponseEntity with status 201 (Created) and with body the new patients, or with status 400 (Bad Request) if the patients has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/patients",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Patients> createPatients(@Valid @RequestBody Patients patients) throws URISyntaxException {
        log.debug("REST request to save Patients : {}", patients);
        if (patients.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("patients", "idexists", "A new patients cannot already have an ID")).body(null);
        }
        Patients result = patientsRepository.save(patients);
        return ResponseEntity.created(new URI("/api/patients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("patients", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /patients : Updates an existing patients.
     *
     * @param patients the patients to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated patients,
     * or with status 400 (Bad Request) if the patients is not valid,
     * or with status 500 (Internal Server Error) if the patients couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/patients",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Patients> updatePatients(@Valid @RequestBody Patients patients) throws URISyntaxException {
        log.debug("REST request to update Patients : {}", patients);
        if (patients.getId() == null) {
            return createPatients(patients);
        }
        Patients result = patientsRepository.save(patients);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("patients", patients.getId().toString()))
            .body(result);
    }

    /**
     * GET  /patients : get all the patients.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of patients in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/patients",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Patients>> getAllPatients(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Patients");
        Page<Patients> page = patientsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/patients");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /patients/:id : get the "id" patients.
     *
     * @param id the id of the patients to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the patients, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/patients/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Patients> getPatients(@PathVariable Long id) {
        log.debug("REST request to get Patients : {}", id);
        Patients patients = patientsRepository.findOne(id);
        return Optional.ofNullable(patients)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /patients/:id : delete the "id" patients.
     *
     * @param id the id of the patients to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/patients/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePatients(@PathVariable Long id) {
        log.debug("REST request to delete Patients : {}", id);
        patientsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("patients", id.toString())).build();
    }

}
