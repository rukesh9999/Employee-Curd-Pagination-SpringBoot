package com.api.employee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.employee.models.Designation;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer>  {

}
