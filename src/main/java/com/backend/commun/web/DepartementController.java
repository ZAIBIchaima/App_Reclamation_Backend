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
import org.springframework.web.bind.annotation.RestController;

import com.backend.commun.entity.Departement;
import com.backend.commun.repository.DepartementRepository;
import com.backend.exception.ResourceNotFoundException;

import com.backend.reclamation.services.ExportDepartementService;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Optional;


@CrossOrigin(origins = "*")
@ RestController
@ RequestMapping ( "/api" )
@PreAuthorize("hasRole('RECLAMATION') or hasRole('ADMIN') or hasRole('STOCK') or hasRole('BO')")
public class DepartementController {
	@Autowired 	DepartementRepository  repository;
	@Autowired
	private ExportDepartementService exportDepartementService;
	
	 @GetMapping("/departements")
	  public List<Departement> getAllDepartements() {
	     System.out.println("Get all departements...");
	 
	    List<Departement> departements = new ArrayList<>();
	    repository.findAll().forEach(departements::add);
	 
	    return departements;
	  }
	 
	 @GetMapping("/departements/{id}")
	public ResponseEntity<Departement> getDepartementById(@PathVariable(value = "id") Long Id)
				throws ResourceNotFoundException {
		Departement Departement = repository.findById(Id).orElseThrow(() -> new ResourceNotFoundException("Departement not found for this id :: " + Id));
		return ResponseEntity.ok().body(Departement);
	}
	 //getDepartementByNom
	 @GetMapping("/departements/5/{libDep}")
		public ResponseEntity<Departement> getByLibDep(@PathVariable (value = "libDep") String libDep) {
		 
		 Departement Departement = repository.findByLibDep(libDep);
		 System.out.println("Departement  :: "+Departement);
		 return ResponseEntity.ok().body(Departement);
			
		}
	 
	
	 @PostMapping("/departements")
	 public Departement createDepartement( @RequestBody Departement departement) {
		 departement.setDateUserCreation(new Date());
		 departement.setDateUserLastmodified(null);
		 return repository.save(departement);
	 }
	 @PutMapping("/departements/{id}")
	  public Departement updateDepartement(@PathVariable(name="id") Long id, @RequestBody Departement Departement){
		     
		 Departement.setDateUserLastmodified(new Date());
		    System.out.println(Departement.toString());
			return repository.save(Departement);
		}

	 @DeleteMapping("/departements/{id}")
		public Map<String,Boolean>deleteDepartement(@PathVariable(value="id")Long DepartementId)
				throws ResourceNotFoundException {
		 Departement Departement = repository.findById(DepartementId).orElseThrow(() -> new ResourceNotFoundException("Departement not found for this id :: " + DepartementId));
			repository.delete(Departement);
			Map<String,Boolean>response=new HashMap<>();
			response.put("deleted", Boolean.TRUE);
			return response;
		
	}
	 @DeleteMapping("/Departements/delete")
		public ResponseEntity<String>deleteAllDepartements(){
			System.out.println("Delete All Departements");
			repository.deleteAll();
			return new ResponseEntity<>("All Departements have been deleted!",HttpStatus.OK);
		}
	 
	//pdf
		@GetMapping(value="/departements/pdf")
		public ResponseEntity <InputStreamResource> pdf(){
			List<Departement> deps= (List<Departement>) repository.findAll();
			ByteArrayInputStream bais = exportDepartementService.depPdf(deps);
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("content-Disposition","inline;filename=departement.pdf");
			return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bais));
		}

	  

}
