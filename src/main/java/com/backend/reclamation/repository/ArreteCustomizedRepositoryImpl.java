package com.backend.reclamation.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.backend.reclamation.entity.Arrete;

public class ArreteCustomizedRepositoryImpl implements ArreteCustomizedRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Arrete> getArreteWithCustomQuery(Date dateStart, Date dateEnd,  String refArrete) {
		List<Arrete> recList = entityManager.createQuery(
				"SELECT a FROM Arrete a WHERE dateArrete BETWEEN ?1 AND ?2 OR refArrete LIKE ?3 ")
				.setParameter(1, dateStart).setParameter(2, dateEnd)
				.setParameter(3, refArrete)
				.getResultList();

		// Do the necessary tasks
		return recList;
	}

}
