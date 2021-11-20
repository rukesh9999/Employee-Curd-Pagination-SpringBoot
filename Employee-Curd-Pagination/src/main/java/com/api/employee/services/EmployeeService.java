package com.api.employee.services;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.api.employee.dto.DesignationResponse;
import com.api.employee.dto.EmployeeRequest;
import com.api.employee.dto.EmployeeResponse;
import com.api.employee.dto.LocationResponse;
import com.api.employee.models.Designation;

public interface EmployeeService {

	public Map<String, Object> getAllEmployees(EmployeeRequest   employeeRequest);

	public String saveEmployee(EmployeeRequest employeeRequest, MultipartFile file,HttpServletRequest request);
	
	public String updateEmployee(EmployeeRequest employeeRequest, MultipartFile file,HttpServletRequest request);

	
	public List<LocationResponse> getAllLocations();
	
	public List<DesignationResponse> getAllDesignations();
	
	public EmployeeResponse getEmployee(Integer id);
	
	
	public String inactiveEmployee(Integer id);
}
