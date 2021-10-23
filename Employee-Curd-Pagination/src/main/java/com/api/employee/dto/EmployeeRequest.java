package com.api.employee.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Data
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeRequest {
	
	private Integer id;
	
	@NotBlank(message = "firstName cannot be null")
	private String firstName;
	
	@NotBlank(message = "lastName cannot be null")
	private String lastName;
	
	@Email
	@NotNull(message = "email cannot be null")
	private String email;
	 
	
	@NotNull(message = "Designation cannot be null")
	private Integer designationId;
	 
	
	@NotNull(message = "location cannot be null")
	private Integer locationId;
	

	
	
	private String filePath;
	
}
