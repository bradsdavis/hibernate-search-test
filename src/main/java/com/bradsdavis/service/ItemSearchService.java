package com.bradsdavis.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetingRequest;

import com.bradsdavis.jpa.model.Item;

@Stateless
@LocalBean
public class ItemSearchService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public Collection<Item> searchByName(String name) {
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
		
		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Item.class).get();
		
		org.apache.lucene.search.Query query = qb.phrase().onField("name").sentence(name).createQuery();
		
		
		javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(query, Item.class);

		List<Item> results = persistenceQuery.getResultList();
		
		System.out.println(results.iterator().next().getCategories());
		
		return results;
	}
	
	public Collection<Item> searchByNameAndCatalog(String name, String catalog) {
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
		
		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Item.class).get();
		
		org.apache.lucene.search.Query nameQuery = qb.phrase().onField("name").sentence(name).createQuery();
		org.apache.lucene.search.Query catalogQuery = qb.phrase().onField("catalog_name").sentence(catalog).createQuery();

		BooleanQuery andQuery = new BooleanQuery();
		andQuery.add(nameQuery, Occur.MUST);
		andQuery.add(catalogQuery, Occur.MUST);
		
		javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(andQuery, Item.class);

		List<Item> results = persistenceQuery.getResultList();
		return results;
	}
	
	public Collection<Facet> getFacets() {
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
		
		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Item.class).get();
		
		FacetingRequest labelFacetingRequest = qb.facet()
		    .name( "categoryLabel" )
		    .onField( "category_name")
		    .discrete()
		    .createFacetingRequest();

        org.apache.lucene.search.Query query = qb.all().createQuery();
        FullTextQuery hibQuery = fullTextEntityManager.createFullTextQuery(query, Item.class);

        FacetManager fm = hibQuery.getFacetManager().enableFaceting(labelFacetingRequest);
        return fm.getFacets("categoryLabel");
	}
}
