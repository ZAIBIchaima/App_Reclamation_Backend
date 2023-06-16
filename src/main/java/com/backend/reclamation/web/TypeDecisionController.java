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

import com.backend.reclamation.entity.Reclamation;
import com.backend.reclamation.entity.SourceExecution;
import com.backend.reclamation.entity.TypeDecision;
import com.backend.reclamation.repository.SourceExecutionRepository;
import com.backend.reclamation.repository.TypeDecisionRepository;
import com.backend.reclamation.services.ExportSourceExecutionService;
import com.backend.reclamation.services.ExportTypeDecisionService;

@CrossOrigin(origins = "*")
@RestController
@PreAuthorize("hasRole('RECLAMATION') or hasRole('ADMIN')")
@RequestMapping("/api")
public class TypeDecisionController {
	
	@Autowired
	private TypeDecisionRepository typeDecisionRepository;
	@Autowired
	private ExportTypeDecisionService exportTypeDecisionService;
	
	@GetMapping(value="/listTypeDecision")
	public List<TypeDecision> listTypeDecisions(){
		return typeDecisionRepository.findAll();
	}
	@GetMapping(value="/listTypeDecision/{id}")
	public TypeDecision listTypeDecisions(@PathVariable(name="id") Long id){
		return typeDecisionRepository.findById(id).get();
	}
	
	@PostMapping(value="/listTypeDecision")
	public TypeDecision createTypeDecision(  @RequestBody TypeDecision TypeDecision){
		TypeDecision.setDateUserCreation(new Date());
		TypeDecision.setDateUserLastmodified(null);
		//save TypeDecision to the database  
		return typeDecisionRepository.save(TypeDecision);
	}
	
	@PutMapping(value="/listTypeDecision/{id}")
	public TypeDecision updateTypeDecision(@PathVariable(name="id") Long id, @RequestBody TypeDecision TypeDecision){
		TypeDecision.setDateUserLastmodified(new Date());
		return typeDecisionRepository.save(TypeDecision);
	}
	
	@DeleteMapping(value="/listTypeDecision/{id}")
	public void deleteTypeDecision(@PathVariable(name="id") Long id){
		typeDecisionRepository.deleteById(id);
	}
	//pdf
	@GetMapping(value="/typeDecision/pdf")
	public ResponseEntity <InputStreamResource> pdf(){
		List<TypeDecision> sourceExecutions= (List<TypeDecision>) typeDecisionRepository.findAll();
		ByteArrayInputStream bais = exportTypeDecisionService.pdf(sourceExecutions);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("content-Disposition","inline;filename=sourceExecutions.pdf");
		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bais));
	}

}
