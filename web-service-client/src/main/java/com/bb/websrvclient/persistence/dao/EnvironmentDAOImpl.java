package com.bb.websrvclient.persistence.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bb.websrvclient.persistence.entity.EnvironmentHeader;

@Repository
public class EnvironmentDAOImpl implements IEnvironmentDAO {

	private static final Logger logger = LoggerFactory.getLogger(EnvironmentDAOImpl.class);

	private SessionFactory sessionFactory;
	
	@Autowired(required = true)
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	@Override
	public void addEnvironment(EnvironmentHeader p) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(p);
		logger.info("Person saved successfully, Person Details=" + p);
	}

	@Override
	public void updateEnvironment(EnvironmentHeader p) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(p);
		logger.info("Person updated successfully, Person Details=" + p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnvironmentHeader> listEnvironments() {
		Session session = this.sessionFactory.getCurrentSession();
		List<EnvironmentHeader> personsList = session.createQuery("from EnvironmentHeader").list();
		for (EnvironmentHeader p : personsList) {
			logger.info("Person List::" + p);
		}
		return personsList;
	}

	@Override
	public EnvironmentHeader findById(String id) {
		Session session = this.sessionFactory.getCurrentSession();
		EnvironmentHeader p = (EnvironmentHeader) session.load(EnvironmentHeader.class, id);
		logger.info("Person loaded successfully, Person details=" + p);
		return p;
	}

	@Override
	public void deleteById(String id) {
		Session session = this.sessionFactory.getCurrentSession();
		EnvironmentHeader p = (EnvironmentHeader) session.load(EnvironmentHeader.class, id);
		if (null != p) {
			session.delete(p);
		}
		logger.info("Person deleted successfully, person details=" + p);
	}

}
