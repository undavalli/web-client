package com.bb.websrvclient.domains;

import java.io.Serializable;
import java.util.List;

public class EnvironmentFormBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String envCode;
	private List<EnvironmentServicesFormBean> services;

	public String getEnvCode() {
		return envCode;
	}

	public void setEnvCode(String envCode) {
		this.envCode = envCode;
	}

	public List<EnvironmentServicesFormBean> getServices() {
		return services;
	}

	public void setServices(List<EnvironmentServicesFormBean> services) {
		this.services = services;
	}
}
