package com.backend.commun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.commun.entity.Departement;
import com.backend.commun.entity.Employe;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long>{
	public Departement findByLibDep(String libDep);
}
