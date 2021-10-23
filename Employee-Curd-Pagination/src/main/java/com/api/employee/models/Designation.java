package com.api.employee.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name="designation",uniqueConstraints = {
		@UniqueConstraint(columnNames ="id"),
		@UniqueConstraint(columnNames ="name")
})
public class Designation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name ="id",unique=true,nullable =false)
	private Integer id;
	
	@Column(name ="name",unique = true,nullable=false)
	private String name;
	
	
	
}
