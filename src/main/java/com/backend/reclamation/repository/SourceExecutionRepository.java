package com.backend.reclamation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.commun.entity.Employe;
import com.backend.reclamation.entity.SourceExecution;

@Repository
public interface SourceExecutionRepository extends JpaRepository<SourceExecution, Long> {
	public SourceExecution findByNom(String nom);

}
