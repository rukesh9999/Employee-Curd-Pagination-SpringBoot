package com.api.employee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.employee.models.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer>{

}
