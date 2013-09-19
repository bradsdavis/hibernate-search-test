package com.bradsdavis.jpa.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Indexed
@Entity
public class Catalog {

	@Id
	@GeneratedValue
	private Long id;

	
	@NotNull
	@Size(min = 3, max = 15)
	@Field(analyze=Analyze.YES, index=Index.YES, store=Store.NO)
	private String name;

	@ContainedIn
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "catalog")
	private Set<Item> items = new HashSet<Item>(0);

	
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


	public Set<Item> getItems() {
		return items;
	}


	public void setItems(Set<Item> items) {
		this.items = items;
	}
	
	
}
