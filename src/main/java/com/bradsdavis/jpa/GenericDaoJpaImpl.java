package com.bradsdavis.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GenericDaoJpaImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

	protected Class<T> entityClass;

	@PersistenceContext
	protected EntityManager entityManager;

	public GenericDaoJpaImpl(Class entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public T create(T t) {
		this.entityManager.persist(t);
		return t;
	}

	@Override
	public T read(PK id) {
		return this.entityManager.find(entityClass, id);
	}

	@Override
	public T update(T t) {
		return this.entityManager.merge(t);
	}

	@Override
	public void delete(T t) {
		t = this.entityManager.merge(t);
		this.entityManager.remove(t);
	}
}