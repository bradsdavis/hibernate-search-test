package com.bradsdavis.jpa.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
public class Item {

	@Id
	@GeneratedValue
	@Column(name = "ITEM_ID", unique = true, nullable = false)
	private Long id;

	@NotNull
	@Size(min=2, max=30)
	@Field(analyze=Analyze.YES, index=Index.YES, store=Store.NO)
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CATALOG_ID", nullable = false)
	@IndexedEmbedded(depth = 1, prefix="catalog_", includePaths={"id", "name"})
	private Catalog catalog;

	@IndexedEmbedded(prefix="category_", includePaths={"name"})
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "item_category", joinColumns = { 
			@JoinColumn(name = "ITEM_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "CATEGORY_ID", nullable = false, updatable = false) })
	private List<Category> categories = new LinkedList<Category>();
	
	public List<Category> getCategories() {
		return categories;
	}
	
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Catalog getCatalog() {
		return catalog;
	}


	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
	
}
