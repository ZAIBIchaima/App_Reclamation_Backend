package com.backend.commun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;


import com.backend.commun.entity.Employe;
import com.backend.reclamation.entity.Reclamation;

@RepositoryRestResource
public interface EmployeRepository extends JpaRepository<Employe, Long>{

	public Employe findByNomPrenom(String nom_prenom);
	
	Boolean existsByEmail(String email);
	

}
