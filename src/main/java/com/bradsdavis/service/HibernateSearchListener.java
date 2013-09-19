package com.bradsdavis.service;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

@Startup
@Singleton
public class HibernateSearchListener {

	@PersistenceContext
	private EntityManager entityManager;
	
	@PostConstruct
	public void atStartup() throws InterruptedException {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		fullTextEntityManager.createIndexer().startAndWait();
	}
	
}
