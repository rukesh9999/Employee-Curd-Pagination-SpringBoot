package com.api.employee.models;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name="loan")
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name ="id",unique =true,nullable=false)
	private Integer id;
	
	@Column(name ="loan_eligible_amount",unique =true,nullable = false)
	private double loanEligibleAmount;
	
	@Column(name ="loan_request_amount",unique =true,nullable = false)
	private double loanRequestAmount;
	
	@Column(name ="loan_amount_per_month",unique =true,nullable = false)
	private double loanAmountPerMonth;
	
	@Column(name ="loan_start_date",unique =true,nullable = false)
	private Date loanstartdate;
	
	@Column(name ="comments",unique =true,nullable = false)
	private  String comments;
	
	@Column(name ="tenure",unique =true,nullable = false)
	private String tenure;
	
	@Column(name ="attachment_path",unique =true,nullable = false)
	private String attachmentPath;
	
	@OneToOne(targetEntity =LoanType.class,cascade =CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "loan_type_id",referencedColumnName ="id" ,unique=false,nullable =false)
	private LoanType loanType;
	
	@ManyToOne(targetEntity =Employee.class, cascade =CascadeType.ALL, fetch = FetchType.EAGER )
	@JoinColumn(name ="employee_id",referencedColumnName = "id" ,unique =false ,nullable =false )
	private Employee employee;
	
	
	
}
