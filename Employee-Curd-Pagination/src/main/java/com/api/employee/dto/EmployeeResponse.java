package com.api.employee.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeResponse {
    private Integer id;
	private String code;
	private String firstName;
	private String lastName;
	private String fullName;
	private String email;
	private String location;
	private String designation;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private String filePath;
	private Integer locationId;
	private Integer designationId;
	
	
	
	
}
