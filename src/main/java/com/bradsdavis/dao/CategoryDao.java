package com.bradsdavis.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.bradsdavis.jpa.GenericDaoJpaImpl;
import com.bradsdavis.jpa.model.Category;


@Stateless
@LocalBean
public class CategoryDao extends GenericDaoJpaImpl<Category, Long> {

	public CategoryDao() {
		super(Category.class);
	}

}
