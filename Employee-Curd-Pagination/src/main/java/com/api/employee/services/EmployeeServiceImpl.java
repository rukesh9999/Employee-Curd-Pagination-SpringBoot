package com.api.employee.services;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.api.employee.dto.DesignationResponse;
import com.api.employee.dto.EmployeeRequest;
import com.api.employee.dto.EmployeeResponse;
import com.api.employee.dto.LocationResponse;
import com.api.employee.exception.NoSuchEmployeeFoundException;
import com.api.employee.models.Designation;
import com.api.employee.models.Employee;
import com.api.employee.models.Location;
import com.api.employee.repositories.DesignationRepository;
import com.api.employee.repositories.EmployeeRepository;
import com.api.employee.repositories.LocationRepository;
import com.api.employee.util.StatusMap;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	private EntityManager entityManager;

	@Override
	public Map<String, Object> getAllEmployees(EmployeeRequest employeeRequest) {

		Page<Employee> employeepages = null;
		LocalDateTime fromDate = null;
		LocalDateTime todate = null;
		Map<String, Object> response = null;

		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Employee> createQuery = criteriaBuilder.createQuery(Employee.class);
			Root<Employee> root = createQuery.from(Employee.class);
			List<Predicate> predicates = new ArrayList<>();

			if (employeeRequest.getFirstName() != null && employeeRequest.getFirstName() != ""
					&& !employeeRequest.getFirstName().equalsIgnoreCase("null")) {
				predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + employeeRequest.getFirstName() + "%"));
			}

			if (employeeRequest.getLastName() != null && employeeRequest.getLastName() != ""
					&& !employeeRequest.getLastName().equalsIgnoreCase("null")) {
				predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + employeeRequest.getLastName() + "%"));
			}

			if (employeeRequest.getEmail() != null && employeeRequest.getEmail() != ""
					&& !employeeRequest.getEmail().equalsIgnoreCase("null")) {
				predicates.add(criteriaBuilder.like(root.get("email"), employeeRequest.getEmail()));
			}

			if (employeeRequest.getLocationId() != null) {
				predicates.add(criteriaBuilder.equal(root.get("location"), employeeRequest.getLocationId()));
			}

			if (employeeRequest.getDesignationId() != null) {

				predicates.add(criteriaBuilder.equal(root.get("designation"), employeeRequest.getDesignationId()));
			}

			if (employeeRequest.getCode() != null && employeeRequest.getCode() != ""
					&& !employeeRequest.getCode().equalsIgnoreCase("null")) {
				predicates.add(criteriaBuilder.equal(root.get("code"), employeeRequest.getCode()));
			}
			
			if(employeeRequest.getFromDate()!=null)
			{
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				fromDate = employeeRequest.getFromDate();
				fromDate=fromDate.withHour(0).withMinute(0).withSecond(0);
				String formatedfromdate = formatter.format(fromDate);
				fromDate =LocalDateTime.parse(formatedfromdate,formatter);
			}
			
			if(employeeRequest.getToDate()!=null)
			{
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				todate = employeeRequest.getToDate();
				todate=todate.withHour(23).withMinute(59).withSecond(59);
				String formatedtodate = formatter.format(todate);
				todate = LocalDateTime.parse(formatedtodate,formatter);
			}
			

			if (employeeRequest.getFromDate() != null && employeeRequest.getToDate() != null) {
				predicates.add(criteriaBuilder.between(root.get("createdDate"), fromDate, todate));
			} else if (employeeRequest.getFromDate() != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), fromDate));
			} else if (employeeRequest.getToDate() != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), todate));
			}

			Predicate[] predicateArray = predicates.toArray(new Predicate[predicates.size()]);

			createQuery.select(root).where(predicateArray).orderBy(criteriaBuilder.desc(root.get("modifiedDate")));

			TypedQuery<Employee> typedQuery = entityManager.createQuery(createQuery);
			int totalrows = typedQuery.getResultList().size();
			Integer pageNumber = employeeRequest.getPageNumber();

			typedQuery.setFirstResult(pageNumber * employeeRequest.getItemsPerPage());
			typedQuery.setMaxResults(employeeRequest.getItemsPerPage());

			PageRequest pageRequest = PageRequest.of(pageNumber , employeeRequest.getItemsPerPage());

			employeepages = new PageImpl<Employee>(typedQuery.getResultList(), pageRequest, totalrows);

			List<EmployeeResponse> employeeResponse = employeepages.getContent().stream()
					.map(this::mapToEmployeeResponse).collect(Collectors.toList());

			response = new LinkedHashMap<>();

			response.put("Employees", employeeResponse);
			response.put("currentPage", employeepages.getNumber());
			response.put("totalElements", employeepages.getTotalElements());
			response.put("totalPages", employeepages.getTotalPages());
		} catch (Exception e) {
          e.printStackTrace();
		}
		return response;

	}

	public EmployeeResponse mapToEmployeeResponse(Employee employee) {

		Location location = locationRepository.findById(employee.getLocation().getId())
				.orElseThrow(() -> new RuntimeException("Location doesnot exists"));

		Designation designation = designationRepository.findById(employee.getDesignation().getId())
				.orElseThrow(() -> new RuntimeException("designation desnot exits"));

		return EmployeeResponse.builder().id(employee.getId()).code(employee.getCode())
				.firstName(employee.getFirstName()).lastName(employee.getLastName())
				.fullName(employee.getFirstName() + "   " + employee.getLastName()).email(employee.getEmail())
				.location(location.getName()).locationId(employee.getLocation().getId())
				.designationId(employee.getDesignation().getId()).designation(designation.getName())
				.createdDate(employee.getCreatedDate()).modifiedDate(employee.getModifiedDate())
				.filePath(employee.getFilePath()).build();

	}

	@Override
	public String saveEmployee(EmployeeRequest employeeRequest, MultipartFile file, HttpServletRequest request) {

		boolean flag = false;
		Employee employee = null;

		if (file != null) {
			String originalFileName = file.getOriginalFilename();
			File destationFolder = new File(request.getContextPath() + "/emp_attachments/");
			// File destationFolder = new
			// File(request.getServletContext().getRealPath("emp_attachments") );
			boolean isFolderExists = destationFolder.exists();
			String modifiedFileName = FilenameUtils.getBaseName(originalFileName) + "_" + System.currentTimeMillis()
					+ "." + FilenameUtils.getExtension(originalFileName);

			if (!isFolderExists) {
				destationFolder.mkdir();
			}

			File serverFile = new File(destationFolder.getPath() + "/".trim() + modifiedFileName.trim());

			try {
				serverFile.createNewFile();
				FileUtils.writeByteArrayToFile(serverFile, file.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			employee = new Employee();
			employee.setFilePath(serverFile.getAbsolutePath());

		} else {
			employee = new Employee();
			employee.setFilePath("");

		}

		/*
		 * Location location =
		 * locationRepository.findById(employeeRequest.getLocationId())
		 * .orElseThrow(()->new RuntimeException("location doesnot exists"));
		 * 
		 * 
		 * 
		 * Designation designation =
		 * designationRepository.findById(employeeRequest.getDesignationId())
		 * .orElseThrow(()->new RuntimeException("designation doesnot exists"));
		 */

		Location location = new Location();
		location.setId(employeeRequest.getLocationId());

		Designation designation = new Designation();
		designation.setId(employeeRequest.getDesignationId());

		employee.setCode(this.generateCode());
		employee.setFirstName(employeeRequest.getFirstName());
		employee.setLastName(employeeRequest.getLastName());
		employee.setEmail(employeeRequest.getEmail());
		employee.setLocation(location);
		employee.setDesignation(designation);
		employee.setCreatedDate(LocalDateTime.now());
		employee.setModifiedDate(LocalDateTime.now());
		employee.setStatusId(StatusMap.Active);
		try {
			employeeRepository.save(employee);
			flag = true;
		} catch (Exception e) {

			flag = false;
			e.printStackTrace();
		}

		if (flag == true) {
			return "Employee saved successfully";
		} else {
			return "Failed to  save successfully";
		}

	}

	@Override
	public String updateEmployee(EmployeeRequest employeeRequest, MultipartFile file, HttpServletRequest request) {

		boolean flag = false;
		Employee employeesave = null;

		employeesave = employeeRepository.findById(employeeRequest.getId())
				.orElseThrow(() -> new RuntimeException("Employee not Found"));

		if (file != null) {
			String originalFileName = file.getOriginalFilename();
			File destationFolder = new File(request.getContextPath() + "/emp_attachments/");
			// File destationFolder = new
			// File(request.getServletContext().getRealPath("emp_attachments") );
			boolean isFolderExists = destationFolder.exists();
			String modifiedFileName = FilenameUtils.getBaseName(originalFileName) + "_" + System.currentTimeMillis()
					+ "." + FilenameUtils.getExtension(originalFileName);

			if (!isFolderExists) {
				destationFolder.mkdir();
			}

			File serverFile = new File(destationFolder.getPath() + "/".trim() + modifiedFileName.trim());

			try {
				serverFile.createNewFile();
				FileUtils.writeByteArrayToFile(serverFile, file.getBytes());
				System.out.println(serverFile.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			employeesave.setId(employeeRequest.getId());
			employeesave.setFilePath(serverFile.getAbsolutePath());

		} else {

			employeesave.setId(employeeRequest.getId());
			employeesave.setFilePath("");

		}

		/*
		 * Location location =
		 * locationRepository.findById(employeeRequest.getLocationId())
		 * .orElseThrow(()->new RuntimeException("location doesnot exists"));
		 * 
		 * 
		 * 
		 * Designation designation =
		 * designationRepository.findById(employeeRequest.getDesignationId())
		 * .orElseThrow(()->new RuntimeException("designation doesnot exists"));
		 */

		Location location = new Location();
		location.setId(employeeRequest.getLocationId());

		Designation designation = new Designation();
		designation.setId(employeeRequest.getDesignationId());

		employeesave.setFirstName(employeeRequest.getFirstName());
		employeesave.setLastName(employeeRequest.getLastName());
		employeesave.setEmail(employeeRequest.getEmail());
		employeesave.setLocation(location);
		employeesave.setDesignation(designation);
		employeesave.setModifiedDate(LocalDateTime.now());

		try {
			employeeRepository.save(employeesave);
			flag = true;
		} catch (Exception e) {

			flag = false;
			e.printStackTrace();
		}

		if (flag == true) {
			return "Employee updated successfully";
		} else {
			return "Failed to  update successfully";
		}

	}

	public String generateCode() {
		final String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom random = new SecureRandom();

		StringBuilder sb = new StringBuilder(4);
		for (int i = 0; i < 4; i++)
			sb.append(str.charAt(random.nextInt(str.length())));
		return sb.toString();

	}

	@Override
	public List<LocationResponse> getAllLocations() {

		return locationRepository.findAll().stream().map(this::mapToLocatinResponse).collect(Collectors.toList());

	}

	@Override
	public List<DesignationResponse> getAllDesignations() {

		return designationRepository.findAll().stream().map(this::mapToDesignationResponse)
				.collect(Collectors.toList());
	}

	public LocationResponse mapToLocatinResponse(Location location) {
		return LocationResponse.builder().id(location.getId()).name(location.getName()).build();
	}

	public DesignationResponse mapToDesignationResponse(Designation designation) {

		return DesignationResponse.builder().id(designation.getId()).name(designation.getName()).build();
	}

	@Override
	public EmployeeResponse getEmployee(Integer id) {

		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new NoSuchEmployeeFoundException("Employee  not found"));

		return this.mapToEmployeeResponse(employee);

	}

	@Override
	public String inactiveEmployee(Integer id) {
		// TODO Auto-generated method stub

		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new NoSuchEmployeeFoundException("Employee no such found"));

		employee.setStatusId(StatusMap.Inactive);

		employeeRepository.save(employee);

		return "Employee Inactived Success";
	}

}
