package com.backend.reclamation.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.backend.reclamation.entity.Reclamation;

public class ReclamationCustomizedRepositoryImpl implements ReclamationCustomizedRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Reclamation> getReclamationWithCustomQuery(Date dateEntreStart, Date dateEntreEnd,String refReclamation) {
		System.out.println("EntreCustomizedRepositoryImpl --> implement");
		List<Reclamation> recList = entityManager
				.createQuery("SELECT r FROM Reclamation r WHERE dateReclamation BETWEEN ?1 AND ?2 OR refReclamation LIKE ?3 ")//OR prenomNomSourceReclamation LIKE ?4
				.setParameter(1, dateEntreStart).setParameter(2, dateEntreEnd)
				.setParameter(3, refReclamation)
				.getResultList();//.setParameter(4, prenomNomSourceReclamation)

		// Do the necessary tasks
		return recList;
	}

	
}
