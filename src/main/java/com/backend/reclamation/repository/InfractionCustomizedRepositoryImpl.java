package com.backend.reclamation.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.backend.reclamation.entity.Infraction;

public class InfractionCustomizedRepositoryImpl implements InfractionCustomizedRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Infraction> getInfractionWithCustomQuery(Date dateEntreStart, Date dateEntreEnd,
			Long numInfraction,String source) {

		List<Infraction> infList = entityManager.createQuery(
				"SELECT i FROM Infraction i WHERE dateInfraction BETWEEN ?1 AND ?2 OR numInfraction = ?3 OR source LIKE ?4 ")
				.setParameter(1, dateEntreStart).setParameter(2, dateEntreEnd)
				.setParameter(3, numInfraction).setParameter(4, source)
				.getResultList();

		// Do the necessary tasks
		return infList;
	}

}
