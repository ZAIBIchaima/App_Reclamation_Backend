package com.backend.reclamation.web;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RestController;

import com.backend.commun.entity.Departement;
import com.backend.reclamation.entity.Entite;
import com.backend.reclamation.entity.Reclamation;
import com.backend.reclamation.repository.EntiteRepository;
import com.backend.reclamation.services.ExportSourceService;




@CrossOrigin(origins = "*")
@RestController
@PreAuthorize("hasRole('RECLAMATION') or hasRole('ADMIN')")
@RequestMapping("/api")
public class EntiteController {
	@Autowired
	private EntiteRepository entiteRepository;
	@Autowired
	private ExportSourceService exportEntiteService;
	
	@GetMapping(value="/entites")
	public List<Entite> listEntites(){
		return entiteRepository.findAll();
	}
	@GetMapping(value="/entites/{id}")
	public Entite listEntites(@PathVariable(name="id") Long id){
		return entiteRepository.findById(id).get();
	}
	
	//getEntiteByNom
	@GetMapping("/entites/5/{nom}")
	public ResponseEntity<Entite> getEntiteByName(@PathVariable (value = "nom") String nom) {
			 
		Entite Entite = entiteRepository.findByNom(nom);
		System.out.println("Entite :: "+Entite);
		return ResponseEntity.ok().body(Entite);
				
	}
	
	@PostMapping(value="/entites")
	public Entite create(  @RequestBody Entite entite){  
		entite.setDateUserCreation(new Date());
		entite.setDateUserLastmodified(null);
		//save reclamation to the database  
		return entiteRepository.save(entite);
		
	}
	
	@PutMapping(value="/entites/{id}")
	public Entite update(@PathVariable(name="id") Long id, @RequestBody Entite Entite){
		Entite.setDateUserLastmodified(new Date());
		return entiteRepository.save(Entite);
	}
	
	@DeleteMapping(value="/entites/{id}")
	public void delete(@PathVariable(name="id") Long id){
		entiteRepository.deleteById(id);
	}
	
	@GetMapping(value="/entites/pdf")
	public ResponseEntity <InputStreamResource> pdf(){
		List<Entite> deps= (List<Entite>) entiteRepository.findAll();
		ByteArrayInputStream bais = exportEntiteService.pdf(deps);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("content-Disposition","inline;filename=source.pdf");
		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bais));
	}

}
