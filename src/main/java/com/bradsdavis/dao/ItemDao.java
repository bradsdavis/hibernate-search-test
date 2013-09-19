package com.bradsdavis.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.bradsdavis.jpa.GenericDaoJpaImpl;
import com.bradsdavis.jpa.model.Item;

@Stateless
@LocalBean
public class ItemDao extends GenericDaoJpaImpl<Item, Long> {
	
	public ItemDao() {
		super(Item.class);
	}
	
}
