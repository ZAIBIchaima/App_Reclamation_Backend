package com.backend.reclamation.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.reclamation.entity.Infraction;
import com.backend.reclamation.entity.Reclamation;
import com.backend.reclamation.repository.ReclamationRepository;
import com.backend.reclamation.services.ExportReclamationService;
import com.backend.security.entity.User;
import com.backend.security.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@PreAuthorize("hasRole('RECLAMATION') or hasRole('ADMIN')")
@RequestMapping("/api")
public class ReclamationWeb {
	@Autowired
	private ReclamationRepository reclamationRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ExportReclamationService exportReclamationService;

	@GetMapping(value = "/listreclamation")
	public List<Reclamation> listReclamations() {
		return reclamationRepository.findAll();
	}

	@GetMapping("/listreclamation/{idReclamation}")
	public Reclamation listReclamations(@PathVariable(name = "idReclamation") Long idReclamation) {
		return reclamationRepository.findById(idReclamation).get();
	}

	@GetMapping("/reclamations/{prenomNomSourceReclamation}")
	public List<Reclamation> getReclamationByName(@PathVariable String prenomNomSourceReclamation) {
		System.out.println("List" + reclamationRepository.findByPrenomNomSourceReclamation(prenomNomSourceReclamation));
		return reclamationRepository.findByPrenomNomSourceReclamation(prenomNomSourceReclamation);

	}

	// recherche multi critere
	@GetMapping(value = "/filterReclamation")
	public List<Reclamation> filter(@RequestParam("dateStart") Date dateStart, 
			@RequestParam("dateEnd") Date dateEnd,
			@RequestParam("refReclamation") String refReclamation) {

		System.out.println("Filter :::: " + dateStart + dateEnd);
		return reclamationRepository.getReclamationWithCustomQuery(dateStart, dateEnd,refReclamation);
	}

	@PostMapping(value = "/listreclamation")
	public Reclamation createReclamation(@RequestBody Reclamation reclamation) {

		reclamation.setDateUserCreation(new Date());
		reclamation.setDateUserLastmodified(null);
		// save reclamation to the database
		return reclamationRepository.save(reclamation);

	}

	@PutMapping(value = "/listreclamation/{id}")
	public Reclamation updateReclamation(@PathVariable(name = "id") Long id, @RequestBody Reclamation Reclamation) {

		Reclamation.setDateUserLastmodified(new Date());
		System.out.println(Reclamation.toString());
		return reclamationRepository.save(Reclamation);
	}

	@DeleteMapping(value = "/listreclamation/{id}")
	public void deleteReclamation(@PathVariable(name = "id") Long id) {
		reclamationRepository.deleteById(id);
	}

	// pdf
	@GetMapping(value = "/export/pdf")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<InputStreamResource> pdfReclamation() {
		// get user
		/*
		 * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 * Optional<User> user=userRepository.findByUsername(auth.getName());
		 * List<Reclamation> reclamations= (List<Reclamation>)
		 * reclamationRepository.findReclamationByUserId(user.get().getId());
		 */
		List<Reclamation> reclamations = (List<Reclamation>) reclamationRepository.findAll();
		ByteArrayInputStream bais = exportReclamationService.reclamationPdf(reclamations);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("content-Disposition", "inline;filename=reclamations.pdf");
		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bais));
	}

	// csv
	@GetMapping(value = "/export/csv")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<InputStreamResource> csvReclamation() throws IOException {
		// get user
		/*
		 * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 * Optional<User> user=userRepository.findByUsername(auth.getName());
		 * List<Reclamation> reclamations= (List<Reclamation>)
		 * reclamationRepository.findReclamationByUserId(user.get().getId());
		 */
		List<Reclamation> reclamations = (List<Reclamation>) reclamationRepository.findAll();
		ByteArrayInputStream bais = exportReclamationService.reclamationCSV(reclamations);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("content-Disposition", "inline;filename=reclamations.xlsx");
		return ResponseEntity.ok().headers(httpHeaders).body(new InputStreamResource(bais));
	}

	@GetMapping(value = "/filterReclamationByDate")
	public List<Reclamation> filterReclamationByDate(@RequestParam("dateReclamationStart") Date dateReclamationStart,
			@RequestParam("dateReclamationEnd") Date dateReclamationEnd) {
		return reclamationRepository.findByDateReclamationBetween(dateReclamationStart, dateReclamationEnd);
	}

	@GetMapping("/reclamation/ref")
	public ResponseEntity<List<Reclamation>> getReclamationByNumAndNom(
			@RequestParam("refReclamation") String refReclamation,
			@RequestParam("prenomNomSourceReclamation") String prenomNomSourceReclamation) {
		// return new
		// ResponseEntity<List<Reclamation>>(reclamationRepository.findByNumReclamationAndPrenomNomSourceReclamation(refReclamation,
		// prenomNomSourceReclamation), HttpStatus.OK);
		System.out.println("Ref+Nom" + reclamationRepository
				.findByRefReclamationAndPrenomNomSourceReclamation(refReclamation, prenomNomSourceReclamation));
		return new ResponseEntity<List<Reclamation>>(reclamationRepository
				.findByRefReclamationAndPrenomNomSourceReclamation(refReclamation, prenomNomSourceReclamation),
				HttpStatus.OK);

	}

}
