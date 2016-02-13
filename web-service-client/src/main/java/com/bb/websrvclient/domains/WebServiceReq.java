package com.bb.websrvclient.domains;

import java.io.Serializable;
import java.util.Map;

public class WebServiceReq implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String enviromentId;
	private String serviceId;
	private String reqContent;
	private String respContent;
	private Map<String, String> enviromentsMap;
	private Map<String, String> envServicesMap;

	public String getEnviromentId() {
		return enviromentId;
	}

	public void setEnviromentId(String enviromentId) {
		this.enviromentId = enviromentId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getReqContent() {
		return reqContent;
	}

	public void setReqContent(String reqContent) {
		this.reqContent = reqContent;
	}

	public String getRespContent() {
		return respContent;
	}

	public void setRespContent(String respContent) {
		this.respContent = respContent;
	}

	public Map<String, String> getEnviromentsMap() {
		return enviromentsMap;
	}

	public void setEnviromentsMap(Map<String, String> enviromentsMap) {
		this.enviromentsMap = enviromentsMap;
	}

	public Map<String, String> getEnvServicesMap() {
		return envServicesMap;
	}

	public void setEnvServicesMap(Map<String, String> envServicesMap) {
		this.envServicesMap = envServicesMap;
	}
}
