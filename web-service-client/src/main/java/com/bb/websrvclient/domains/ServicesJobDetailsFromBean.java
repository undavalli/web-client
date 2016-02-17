package com.bb.websrvclient.domains;

import java.io.Serializable;

public class ServicesJobDetailsFromBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String envCode;
	private String serviceCode;
	private String reqContFileName;
	private String cronExp;
	private String notificationEmail;

	public String getEnvCode() {
		return envCode;
	}

	public void setEnvCode(String envCode) {
		this.envCode = envCode;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getReqContFileName() {
		return reqContFileName;
	}

	public void setReqContFileName(String reqContFileName) {
		this.reqContFileName = reqContFileName;
	}

	public String getCronExp() {
		return cronExp;
	}

	public void setCronExp(String cronExp) {
		this.cronExp = cronExp;
	}

	public String getNotificationEmail() {
		return notificationEmail;
	}

	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}
	
	public String getJobName(){
		return getEnvCode() + "_" + getServiceCode();
	}
}
