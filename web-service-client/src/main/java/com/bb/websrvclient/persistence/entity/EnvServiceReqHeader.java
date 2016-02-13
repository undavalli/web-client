package com.bb.websrvclient.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ENVIRONMENT_SRV_HEADER")
public class EnvServiceReqHeader {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(nullable = false)
	private String id;
	
	@Column(name="ENVIRONMENT_SRV_ID")
	private String enviromentSrvId;
	
	@Column(name = "HEADER_CODE", length = 1000)
	private String headerCode;

	@Column(name = "HEADER_VALUE", length = 4000)
	private String headerValue;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnviromentSrvId() {
		return enviromentSrvId;
	}

	public void setEnviromentSrvId(String enviromentSrvId) {
		this.enviromentSrvId = enviromentSrvId;
	}

	public String getHeaderCode() {
		return headerCode;
	}

	public void setHeaderCode(String headerCode) {
		this.headerCode = headerCode;
	}

	public String getHeaderValue() {
		return headerValue;
	}

	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}
}
