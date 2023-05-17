package com.backend.commun.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Optional;

import com.backend.exception.ResourceNotFoundException;
import com.backend.reclamation.entity.Reclamation;
import com.backend.reclamation.entity.SourceExecution;
import com.backend.reclamation.services.ExportEmployeService;
import com.backend.commun.entity.*;
import com.backend.commun.repository.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('RECLAMATION') or hasRole('ADMIN') or hasRole('STOCK') or hasRole('BO')")
public class EmployeController {
	@Autowired
	EmployeRepository repository;
	@Autowired
	private ExportEmployeService exportEmployeService;

	@GetMapping("/employes")
	public List<Employe> getAllEmployes() {
		System.out.println("Get all employes...");
		List<Employe> employes = new ArrayList<>();
		repository.findAll().forEach(employes::add);
		return employes;
	}

	@GetMapping("/employes/{id}")
	public ResponseEntity<Employe> getEmployeById(@PathVariable(value = "id") Long Id)
			throws ResourceNotFoundException {
		Employe Employe = repository.findById(Id)
				.orElseThrow(() -> new ResourceNotFoundException("Employe not found for this id :: " + Id));
		System.out.println("Employe 11 :: " + Employe);
		return ResponseEntity.ok().body(Employe);
	}

	// getEmployeByNom
	@GetMapping("/employes/5/{nomPrenom}")
	public ResponseEntity<Employe> getLaptopsByName(@PathVariable(value = "nomPrenom") String nomPrenom) {

		Employe Employe = repository.findByNomPrenom(nomPrenom);
		System.out.println("Employe :: " + Employe);
		return ResponseEntity.ok().body(Employe);

	}

	@PostMapping("/employes")
	public Employe createEmploye(@RequestBody Employe employe) throws ResourceNotFoundException {
		String email = employe.getEmail();
		if (repository.existsByEmail(email)) {
			throw new ResourceNotFoundException("Email already exists: " + email);
		}
		employe.setDateUserCreation(new Date());
		employe.setDateUserLastmodified(null);
		return repository.save(employe);
	}

	@PutMapping("/employes/{id}")
	public Employe updateEmploye(@PathVariable(name = "id") Long id, @RequestBody Employe Employe) {

		Employe.setDateUserLastmodified(new Date());
		System.out.println(Employe.toString());
		return repository.save(Employe);
	}

	@DeleteMapping("/employes/{id}")
	public Map<String, Boolean> deleteEmploye(@PathVariable(value = "id") Long EmployeId)
			throws ResourceNotFoundException {
		Employe Employe = repository.findById(EmployeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employe not found for this id :: " + EmployeId));
		repository.delete(Employe);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;

	}

	@DeleteMapping("/Employes/delete")
	public ResponseEntity<String> deleteAllEmployes() {
		System.out.println("Delete All Employes");
		repository.deleteAll();
		return new ResponseEntity<>("All Employes have been deleted!", HttpStatus.OK);
	}

	// pdf
	@GetMapping(value = "/employes/pdf")
	public ResponseEntity<InputStreamResource> pdf() {
		List<Employe> sourceExecutions = (List<Employe>) repository.findAll();
		ByteArrayInputStream bais = exportEmployeService.pdf(sourceExecutions);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("content-Disposition", "inline;filename=sourceExecutions.pdf");
		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bais));
	}

}
