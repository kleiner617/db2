package com.vcu.db2.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vcu.db2.domain.Employees1;

import com.vcu.db2.repository.Employees1Repository;
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
 * REST controller for managing Employees1.
 */
@RestController
@RequestMapping("/api")
public class Employees1Resource {

    private final Logger log = LoggerFactory.getLogger(Employees1Resource.class);
        
    @Inject
    private Employees1Repository employees1Repository;

    /**
     * POST  /employees-1-s : Create a new employees1.
     *
     * @param employees1 the employees1 to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employees1, or with status 400 (Bad Request) if the employees1 has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employees-1-s",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employees1> createEmployees1(@Valid @RequestBody Employees1 employees1) throws URISyntaxException {
        log.debug("REST request to save Employees1 : {}", employees1);
        if (employees1.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employees1", "idexists", "A new employees1 cannot already have an ID")).body(null);
        }
        Employees1 result = employees1Repository.save(employees1);
        return ResponseEntity.created(new URI("/api/employees-1-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employees1", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employees-1-s : Updates an existing employees1.
     *
     * @param employees1 the employees1 to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employees1,
     * or with status 400 (Bad Request) if the employees1 is not valid,
     * or with status 500 (Internal Server Error) if the employees1 couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employees-1-s",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employees1> updateEmployees1(@Valid @RequestBody Employees1 employees1) throws URISyntaxException {
        log.debug("REST request to update Employees1 : {}", employees1);
        if (employees1.getId() == null) {
            return createEmployees1(employees1);
        }
        Employees1 result = employees1Repository.save(employees1);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employees1", employees1.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employees-1-s : get all the employees1S.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of employees1S in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/employees-1-s",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Employees1>> getAllEmployees1S(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Employees1S");
        Page<Employees1> page = employees1Repository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employees-1-s");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /employees-1-s/:id : get the "id" employees1.
     *
     * @param id the id of the employees1 to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employees1, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employees-1-s/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employees1> getEmployees1(@PathVariable Long id) {
        log.debug("REST request to get Employees1 : {}", id);
        Employees1 employees1 = employees1Repository.findOne(id);
        return Optional.ofNullable(employees1)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employees-1-s/:id : delete the "id" employees1.
     *
     * @param id the id of the employees1 to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/employees-1-s/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployees1(@PathVariable Long id) {
        log.debug("REST request to delete Employees1 : {}", id);
        employees1Repository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employees1", id.toString())).build();
    }

}
