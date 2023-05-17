package com.backend.reclamation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.backend.reclamation.entity.EtatArrete;

@Repository
public interface EtatArreteRepository extends JpaRepository<EtatArrete, Long> {

}
