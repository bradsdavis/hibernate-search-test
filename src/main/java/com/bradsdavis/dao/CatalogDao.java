package com.bradsdavis.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.bradsdavis.jpa.GenericDaoJpaImpl;
import com.bradsdavis.jpa.model.Catalog;

@Stateless
@LocalBean
public class CatalogDao extends GenericDaoJpaImpl<Catalog, Long> {
	
	public CatalogDao() {
		super(Catalog.class);
	}
}
