package com.api.employee.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.employee.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

   
	Page<Employee> findByFirstNameAndLastNameContaining(String firstName,String lastName, Pageable paging2);

	@EntityGraph(attributePaths={"designation","location"},type=EntityGraphType.FETCH)
	Page<Employee> findAllBystatusId(Integer active, Pageable paging);

	
}
