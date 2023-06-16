package com.backend.reclamation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.reclamation.entity.TypeDecision;

@Repository
public interface TypeDecisionRepository extends JpaRepository<TypeDecision, Long> {

}
