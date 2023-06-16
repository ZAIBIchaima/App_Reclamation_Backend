package com.backend.reclamation.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.backend.reclamation.entity.Infraction;
import com.backend.reclamation.services.CountEtat;


@CrossOrigin(origins = "http://localhost:4200")
@RepositoryRestResource
public interface InfractionRepository extends JpaRepository<Infraction, Long>,InfractionCustomizedRepository {
	//SELECT * FROM `infraction` WHERE date_infraction BETWEEN '2023-01-30' AND '2023-02-07';
	@Query("SELECT i FROM Infraction i WHERE dateInfraction BETWEEN ?1 AND ?2")
	List<Infraction> findByDateInfractionBetween(Date date, Date date2);
	
	//@Query("SELECT i FROM Infraction i WHERE numInfraction = ?1 AND source LIKE %?1%")
	List<Infraction> findByNumInfractionAndSource(Long numInfraction, String source);
	
	
	@Query(value="select new com.backend.reclamation.services.CountEtat(COUNT(*)/(SELECT COUNT(*)FROM Infraction)*100, etat) FROM Infraction GROUP BY etat")
	public List<CountEtat> getPercentageGroupByEtat();
}
