package com.security.JWT.Bean;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "M_PLAN")
@Data
public class Plan {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "M_PLAN_ID")
	private int planId;

	@Column(name = "M_PLAN_NAME")
	private String planName;

	@Column(name = "M_PRIORITY_NUMBER")
	private String priority;

	@Column(name = "CREATED_DATE")
	private Date createdDt;

	@Column(name = "UPDATED_DATE")
	private Date updatedDt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;
}
