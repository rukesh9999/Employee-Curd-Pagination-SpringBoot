package com.api.employee.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "location",uniqueConstraints = {
		@UniqueConstraint(columnNames ="id"),
		@UniqueConstraint(columnNames = "name")
})
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="id",unique =true,nullable=false)
	private Integer id;
	
	@Column(name="name",unique =true,nullable =false)
	private String name;
	
	/*
	 * @OneToMany(targetEntity =Employee.class,fetch = FetchType.LAZY, mappedBy =
	 * "location") private Set<Employee> employee;
	 */
	
}
