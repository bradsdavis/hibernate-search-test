package com.bradsdavis.hibernate.search;

import java.io.Serializable;
import java.util.Properties;

import org.apache.lucene.document.Document;
import org.hibernate.search.FullTextFilter;
import org.hibernate.search.filter.FullTextFilterImplementor;
import org.hibernate.search.indexes.spi.IndexManager;
import org.hibernate.search.store.IndexShardingStrategy;

/**
 * Not currently in use.
 * 
 * @author bradsdavis
 *
 */
public class CatalogShardingStrategy implements IndexShardingStrategy {

	private IndexManager[] indexManagers;

	@Override
	public void initialize(Properties properties, IndexManager[] providers) {
		this.indexManagers = indexManagers;
	}

	@Override
	public IndexManager[] getIndexManagersForAllShards() {
		return indexManagers;
	}

	@Override
	public IndexManager getIndexManagerForAddition(Class<?> entity, Serializable id, String idInString, Document document) {
		Integer catalogId = Integer.parseInt(document.getFieldable("catalog_id").stringValue());
		System.out.println("Catalog Id: "+catalogId);
		
		return indexManagers[catalogId];
	}

	@Override
	public IndexManager[] getIndexManagersForDeletion(Class<?> entity,
			Serializable id, String idInString) {
		return getIndexManagersForAllShards();
	}

	@Override
	public IndexManager[] getIndexManagersForQuery(FullTextFilterImplementor[] filters) {
		
		FullTextFilter filter = getNamedFilter(filters, "catalog_aware");
		if (filter == null) {
			return getIndexManagersForAllShards();
		} else {
			return new IndexManager[] { indexManagers[Integer.parseInt(filter.getParameter("catalog_id").toString())] };
		}
	}

	private FullTextFilter getNamedFilter(FullTextFilterImplementor[] filters, String name) {
		for (FullTextFilterImplementor filter : filters) {
			if (filter.getName().equals(name))
				return filter;
		}
		return null;
	}

}
