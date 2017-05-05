package com.vcu.db2.repository;

import com.vcu.db2.domain.Employees1;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Employees1 entity.
 */
@SuppressWarnings("unused")
public interface Employees1Repository extends JpaRepository<Employees1,Long> {

}
