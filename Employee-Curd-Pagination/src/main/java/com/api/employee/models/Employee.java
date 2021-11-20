package com.api.employee.models;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name ="employee",uniqueConstraints = {		
		@UniqueConstraint(columnNames ="id"),
		@UniqueConstraint(columnNames = "code"),
		@UniqueConstraint(columnNames = "email")
})
public class Employee {

	@Id
	@GeneratedValue(strategy =GenerationType.SEQUENCE)
	
	@Column(name ="id",unique =true,nullable =false)
	private Integer id;
	
	@Column(name ="first_name",unique =false,nullable =false)
	private String firstName;
	
	@Column(name ="last_name",unique =false,nullable =false)
	private String lastName;
	
	@Column(name ="email",unique =true,nullable =false)
	private String email;
	
	@Column(name ="code",unique =true,nullable =false)
	private String code;
	
	@ManyToOne(targetEntity=Designation.class,fetch = FetchType.LAZY)
	@JoinColumn(name = "designation_id", referencedColumnName ="id",unique =false,nullable = false)
	private Designation designation;
	
	@ManyToOne(targetEntity=Location.class, fetch=FetchType.LAZY)
	@JoinColumn(name="location_id",referencedColumnName ="id",unique =false,nullable = false)
	private Location location;
	
	
	@Column(name="created_date",unique =false,nullable =false)
	private LocalDateTime createdDate;
	
	@Column(name="modified_date",unique =false,nullable =false)
	private LocalDateTime modifiedDate;
	
	@Column(name="file_path",unique =false,nullable =true)
	private String filePath; 
	
	@Column(name="status_id",unique =false,nullable =false)
	private Integer statusId;
	
	
}
