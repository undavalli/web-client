package com.bb.websrvclient.domains;

import java.util.List;

public class EnvironmentServicesFormBean {

	private String serviceCode;

	private String serviceURL;

	private List<EnvServiceReqHeaderFormBean> headers;

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

	public List<EnvServiceReqHeaderFormBean> getHeaders() {
		return headers;
	}

	public void setHeaders(List<EnvServiceReqHeaderFormBean> headers) {
		this.headers = headers;
	}
}
