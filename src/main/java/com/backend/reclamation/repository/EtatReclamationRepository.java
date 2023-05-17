package com.backend.reclamation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.reclamation.entity.EtatReclamation;
@Repository
public interface EtatReclamationRepository extends JpaRepository<EtatReclamation, Long> {

}
