package com.backend.reclamation.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.backend.reclamation.entity.Infraction;
import com.backend.reclamation.entity.Reclamation;


@CrossOrigin(origins = "http://localhost:4200")
@RepositoryRestResource
public interface ReclamationRepository extends JpaRepository<Reclamation, Long>,ReclamationCustomizedRepository {
	//list of 'Reclamation' between two date
	@Query("SELECT r FROM Reclamation r WHERE dateReclamation BETWEEN ?1 AND ?2")
	List<Reclamation> findByDateReclamationBetween(Date date, Date date2);
	
	//list of 'Reclamation' By 'ref_Reclamation' and 'prenom_nom_source_reclamation'
	@Query("SELECT r FROM Reclamation r WHERE refReclamation LIKE %?1% ")
	List<Reclamation> findByRefReclamationAndPrenomNomSourceReclamation(String refReclamation, String prenomNomSourceReclamation);
	
	//** test **/
	@RestResource(path="/bynum")
	public List<Reclamation> findByNumReclamationContains(@Param("mc") int a);
	//Search nom source reclamation
	public List<Reclamation> findByPrenomNomSourceReclamation(String prenomNomSourceReclamation);
	
	/*@Query(value="SELECT * FROM reclamation R WHERE R.user_creation = :UserCreation",nativeQuery=true)
	public List<Reclamation> findReclamationByUserId(Long id);*/
	
	//list of 'Reclamation' By 'prenomNomSourceDestinataire' and 'prenom_nom_source_reclamation'
	//@Query("SELECT r FROM reclamation r WHERE r.prenom_nom_source-reclamation LIKE %?1% AND r.prenom_nom_source_destinataire LIKE %?1%")
	List<Reclamation> findByAndPrenomNomSourceReclamationAndPrenomNomSourceDestinataire(String prenomNomSourceReclamation,String prenomNomSourceDestinataire);
	

}
