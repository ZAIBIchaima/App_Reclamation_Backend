package com.backend.reclamation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.reclamation.entity.Entite;

public interface EntiteRepository extends JpaRepository<Entite, Long> {
	public Entite findByNom(String nom);

}
