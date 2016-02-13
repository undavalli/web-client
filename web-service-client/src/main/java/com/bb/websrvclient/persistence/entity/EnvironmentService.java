package com.bb.websrvclient.persistence.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ENVIRONMENT_SERVICES")
public class EnvironmentService {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(nullable = false)
	private String id;

	@Column(name = "ENVIRONMENT_ID")
	private String enviromentId;

	@Column(name = "SERVICE_CODE", length = 50)
	private String serviceCode;

	@Column(name = "SERVICE_URL", length = 4000)
	private String serviceURL;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "ENVIRONMENT_SRV_ID", referencedColumnName = "ID")
	private List<EnvServiceReqHeader> headers;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnviromentId() {
		return enviromentId;
	}

	public void setEnviromentId(String enviromentId) {
		this.enviromentId = enviromentId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public List<EnvServiceReqHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(List<EnvServiceReqHeader> headers) {
		this.headers = headers;
	}
}
