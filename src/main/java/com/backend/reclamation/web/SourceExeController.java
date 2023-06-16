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

import com.backend.commun.entity.Employe;
import com.backend.reclamation.entity.Infraction;
import com.backend.reclamation.entity.Reclamation;
import com.backend.reclamation.entity.SourceExecution;
import com.backend.reclamation.repository.InfractionRepository;
import com.backend.reclamation.repository.SourceExecutionRepository;
import com.backend.reclamation.services.ExportSourceExecutionService;

@CrossOrigin(origins = "*")
@RestController
@PreAuthorize("hasRole('RECLAMATION') or hasRole('ADMIN')")
@RequestMapping("/api")
public class SourceExeController {
	
	@Autowired
	private SourceExecutionRepository sourceExecutionRepository;
	@Autowired
	private ExportSourceExecutionService exportSourceExecutionService;
	
	@GetMapping(value="/listSourceExecution")
	public List<SourceExecution> listSourceExecutions(){
		return sourceExecutionRepository.findAll();
	}
	@GetMapping(value="/listSourceExecution/{id}")
	public SourceExecution listsourceExecutions(@PathVariable(name="id") Long id){
		return sourceExecutionRepository.findById(id).get();
	}
	
	//getSourceByNom
		 @GetMapping("/sourceExecution/5/{nom}")
			public ResponseEntity<SourceExecution> getSourceExecutionByName(@PathVariable (value = "nom") String nom) {
			 
			 SourceExecution SourceExecution = sourceExecutionRepository.findByNom(nom);
			 System.out.println("SourceExecution :: "+SourceExecution);
			 return ResponseEntity.ok().body(SourceExecution);
				
			}
	
	@PostMapping(value="/listSourceExecution")
	public SourceExecution createReclamation(  @RequestBody SourceExecution sourceExecution){  
		sourceExecution.setDateUserCreation(new Date());
		sourceExecution.setDateUserLastmodified(null);
		//save reclamation to the database  
		return sourceExecutionRepository.save(sourceExecution);
		
	}
	
	@PutMapping(value="/listSourceExecution/{id}")
	public SourceExecution updateSourceExecution(@PathVariable(name="id") Long id, @RequestBody SourceExecution SourceExecution){
		SourceExecution.setDateUserLastmodified(new Date());
		return sourceExecutionRepository.save(SourceExecution);
	}
	
	@DeleteMapping(value="/listSourceExecution/{id}")
	public void deleteSourceExecution(@PathVariable(name="id") Long id){
		sourceExecutionRepository.deleteById(id);
	}
	
	//pdf
			@GetMapping(value="/sourceExecution/pdf")
			public ResponseEntity <InputStreamResource> pdf(){
				List<SourceExecution> sourceExecutions= (List<SourceExecution>) sourceExecutionRepository.findAll();
				ByteArrayInputStream bais = exportSourceExecutionService.pdf(sourceExecutions);
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.add("content-Disposition","inline;filename=sourceExecutions.pdf");
				return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bais));
			}

}
