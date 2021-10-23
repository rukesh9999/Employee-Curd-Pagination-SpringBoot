package com.api.employee.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.employee.dto.DesignationResponse;
import com.api.employee.dto.EmployeeRequest;
import com.api.employee.dto.EmployeeResponse;
import com.api.employee.dto.LocationResponse;
import com.api.employee.models.Employee;
import com.api.employee.services.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
@CrossOrigin("*")
@RequestMapping("/loan")
@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService; 
	
	@GetMapping("/all")
	public ResponseEntity<Map<String, Object>> getEmployees(@RequestParam(required =false) String firstName,
			                                                @RequestParam(required = false)String lastName,
			                                                @RequestParam(defaultValue ="0")int page,
			                                                @RequestParam(defaultValue ="3")int size)
	{
		Map<String, Object>  employees = employeeService.getAllEmployees(firstName,lastName,page,size); 
		return new ResponseEntity<Map<String, Object>>(employees,HttpStatus.OK);
	}
	
	
	@PostMapping("/saveEmployee")
	public ResponseEntity<String> saveEmployee(@RequestParam(value = "employee",required = false) String  employee,@RequestParam(value = "file",required = false)
	                                          MultipartFile file,HttpServletRequest request)
	{
	    ObjectMapper mapper = new ObjectMapper();
	    String response=null;

		EmployeeRequest employeeRequest;
		try {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

			employeeRequest = mapper.readValue(employee,EmployeeRequest.class);
			 response = employeeService.saveEmployee(employeeRequest,file,request);
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<String>(response,HttpStatus.OK);
		
		
	}
	
	
	
	@PutMapping("/updateEmployee")
	public ResponseEntity<String> updateEmployee(@RequestParam(value = "employee",required = false) String  employee,@RequestParam(value = "file",required = false)
	                                          MultipartFile file,HttpServletRequest request)
	{
	    ObjectMapper mapper = new ObjectMapper();
	    String response=null;

		EmployeeRequest employeeRequest;
		try {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

			employeeRequest = mapper.readValue(employee,EmployeeRequest.class);
			response = employeeService.updateEmployee(employeeRequest,file,request);
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<String>(response,HttpStatus.OK);
		
		
	}
	
	

	@GetMapping("getEmployee/{id}")
	public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable("id") Integer id)
	{
		EmployeeResponse employeeResponse = employeeService.getEmployee(id);
		return new ResponseEntity<EmployeeResponse>(employeeResponse,HttpStatus.OK);
	}
	
	
	@DeleteMapping("inactive/{id}")
	public ResponseEntity<String> inactiveEmployee(@PathVariable("id")Integer id)
	{
		String message  = employeeService.inactiveEmployee(id);
		
		return new ResponseEntity<String>(message,HttpStatus.OK);
		
	}
	
	
	
	@GetMapping("/locations")
	public ResponseEntity<List<LocationResponse>> getAllLocations()
	{
		List<LocationResponse> locations =   employeeService.getAllLocations();
		
		return new  ResponseEntity<List<LocationResponse>>(locations,HttpStatus.OK);
	}
	
	@GetMapping("/designations")
	public ResponseEntity<List<DesignationResponse>> getAllDesignations()
	{
		List<DesignationResponse> designations =  employeeService.getAllDesignations();
		return new ResponseEntity<List<DesignationResponse>>(designations,HttpStatus.OK);
	}
	
	
}
