package com.backend.reclamation.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.reclamation.entity.EtatReclamation;
import com.backend.reclamation.entity.Reclamation;
import com.backend.reclamation.repository.EtatReclamationRepository;
import com.backend.reclamation.repository.ReclamationRepository;

@CrossOrigin(origins = "*")
@RestController
@PreAuthorize("hasRole('RECLAMATION') or hasRole('ADMIN')")
@RequestMapping("/api")
public class EtatReclamationController {
	@Autowired
	private EtatReclamationRepository etatreclamationRepository;
	
	@GetMapping(value="/listEtatreclamation")	
	public List<EtatReclamation> listEtatReclamations(){
		return etatreclamationRepository.findAll();
	}

}
