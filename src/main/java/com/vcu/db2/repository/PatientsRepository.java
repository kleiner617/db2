package com.vcu.db2.repository;

import com.vcu.db2.domain.Patients;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Patients entity.
 */
@SuppressWarnings("unused")
public interface PatientsRepository extends JpaRepository<Patients,Long> {

}
