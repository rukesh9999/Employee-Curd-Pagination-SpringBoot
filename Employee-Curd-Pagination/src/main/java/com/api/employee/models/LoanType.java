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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Data
@Entity
@Table(name="loan_type",uniqueConstraints = {
		@UniqueConstraint(columnNames = "id"),
		@UniqueConstraint(columnNames = "type")
})
public class LoanType {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="id",unique =true,nullable = false)
	private Integer id;
	
	@Column(name="type",unique =true,nullable = false)
	private String type;
	
}
