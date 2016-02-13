package com.bb.websrvclient.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bb.websrvclient.persistence.dao.IEnvironmentDAO;
import com.bb.websrvclient.persistence.entity.EnvironmentHeader;

@Service
public class EnvironmentServiceImpl implements IEnvironmentService {
	
	@Autowired
	private IEnvironmentDAO environmentDAO;

	public void setEnvironmentDAO(IEnvironmentDAO environmentDAO) {
		this.environmentDAO = environmentDAO;
	}

	@Override
	@Transactional
	public void addEnvironment(EnvironmentHeader eh) {
		this.environmentDAO.addEnvironment(eh);
	}

	@Override
	@Transactional
	public void updateEnvironment(EnvironmentHeader eh) {
		this.environmentDAO.updateEnvironment(eh);
	}

	@Override
	@Transactional
	public List<EnvironmentHeader> listEnvironments() {
		return this.environmentDAO.listEnvironments();
	}

	@Override
	@Transactional
	public EnvironmentHeader findById(String id) {
		return this.environmentDAO.findById(id);
	}

	@Override
	@Transactional
	public void deleteById(String id) {
		this.environmentDAO.deleteById(id);
	}

}
