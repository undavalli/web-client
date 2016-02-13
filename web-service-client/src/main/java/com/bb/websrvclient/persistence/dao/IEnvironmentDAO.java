package com.bb.websrvclient.persistence.dao;

import java.util.List;

import com.bb.websrvclient.persistence.entity.EnvironmentHeader;

public interface IEnvironmentDAO{
	
	public void addEnvironment(EnvironmentHeader eh);
	public void updateEnvironment(EnvironmentHeader eh);
	public List<EnvironmentHeader> listEnvironments();
	public EnvironmentHeader findById(String id);
	public void deleteById(String id);
}
