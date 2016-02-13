package com.bb.websrvclient.persistence.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ENVIRONMENT_HEADER")
public class EnvironmentHeader {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(nullable = false)
	private String id;
	
	@Column(name = "ENV_CODE", length = 50)
	private String envCode;
	
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "ENVIRONMENT_ID", referencedColumnName = "ID")
	private List<EnvironmentService> services;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnvCode() {
		return envCode;
	}

	public void setEnvCode(String envCode) {
		this.envCode = envCode;
	}

	public List<EnvironmentService> getServices() {
		return services;
	}

	public void setServices(List<EnvironmentService> services) {
		this.services = services;
	}
}
