package com.backend.reclamation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.backend.reclamation.entity.EtatInfraction;
@CrossOrigin(origins = "http://localhost:4200")
@RepositoryRestResource
public interface EtatInfractionRepository extends JpaRepository<EtatInfraction, Long> {

}
