package com.backend.reclamation.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.backend.reclamation.entity.Arrete;
import com.backend.reclamation.entity.Reclamation;

@Repository
public interface ArreteRepository extends JpaRepository<Arrete, Long>,ArreteCustomizedRepository {
	@Query("SELECT a FROM Arrete a WHERE dateArrete BETWEEN ?1 AND ?2")
	List<Arrete> findByDateArreteBetween(Date date, Date date2);
	
	//@Query("SELECT a FROM Arrete a WHERE a.numArrete = ?1 AND a.refArrete LIKE %?1%")
	//List<Arrete> findByNumArreteAndRefArrete(int numArrete, String refArrete);
	
	//@Query(value = "SELECT * FROM arrete a WHERE a.num_Arrete =?1 AND a.ref_Arrete LIKE %?1%", nativeQuery = true)
	List<Arrete> findByNumArreteAndRefArrete(int numArrete,String refArrete);
	

}
