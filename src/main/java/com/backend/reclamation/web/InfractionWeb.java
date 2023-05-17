package com.backend.reclamation.web;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

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

import com.backend.reclamation.entity.Infraction;
import com.backend.reclamation.entity.Reclamation;
import com.backend.reclamation.repository.InfractionRepository;
import com.backend.reclamation.services.CountEtat;
import com.backend.reclamation.services.ExportInfractionService;
import com.backend.reclamation.services.ExportReclamationService;

@CrossOrigin(origins = "*")
@RestController
@PreAuthorize("hasRole('RECLAMATION') or hasRole('ADMIN')")
@RequestMapping("/api")
public class InfractionWeb {

	@Autowired
	private InfractionRepository infractionRepository;
	@Autowired
	private ExportInfractionService exportInfractionService;

	@GetMapping(value = "/listinfraction")
	public List<Infraction> listInfractions() {
		return infractionRepository.findAll();
	}

	@GetMapping(value = "/listinfraction/{idInfraction}")
	public Infraction listinfractions(@PathVariable(name = "idInfraction") Long idInfraction) {
		return infractionRepository.findById(idInfraction).get();
	}

	@GetMapping(value = "/listCountEtats")
	public List<CountEtat> listCountEtats() {
		return infractionRepository.getPercentageGroupByEtat();
	}

	// recherche multi critere
	@GetMapping(value = "/filterInfraction")
	public List<Infraction> filter(@RequestParam("dateStart") Date dateStart, @RequestParam("dateEnd") Date dateEnd,
			@RequestParam("numInfraction") Long numInfraction,
			@RequestParam("source") String source) {

		System.out.println("Filter :::: " + dateStart + dateEnd);
		return infractionRepository.getInfractionWithCustomQuery(dateStart, dateEnd, numInfraction,source);
	}

	@GetMapping(value = "/filterInfractionByDate")
	public List<Infraction> filterInfractionByDate(@RequestParam("dateInfractionStart") Date dateInfractionStart,
			@RequestParam("dateInfractionEnd") Date dateInfractionEnd) {
		/*
		 * Date dateInfractionStart =new Date(); Date dateInfractionEnd =null;
		 */
		System.out.println("Filter :::: "
				+ infractionRepository.findByDateInfractionBetween(dateInfractionStart, dateInfractionEnd));

		return infractionRepository.findByDateInfractionBetween(dateInfractionStart, dateInfractionEnd);
	}

	@GetMapping("/infractions/numEmp")
	public ResponseEntity<List<Infraction>> getLaptopsByNameAndBrand(@RequestParam("numInfraction") Long numInfraction,
			@RequestParam("source") String source) {
		return new ResponseEntity<List<Infraction>>(
				infractionRepository.findByNumInfractionAndSource(numInfraction, source), HttpStatus.OK);
	}

	@PostMapping(value = "/listinfraction")
	public Infraction createInfraction(@RequestBody Infraction infraction) {
		infraction.setDateUserCreation(new Date());
		infraction.setDateUserLastmodified(null);
		return infractionRepository.save(infraction);
	}

	@PutMapping(value = "/listinfraction/{idInfraction}")
	public Infraction updateInfraction(@PathVariable(name = "idInfraction") Long idInfraction,
			@RequestBody Infraction infraction) {
		infraction.setDateUserLastmodified(new Date());
		return infractionRepository.save(infraction);
	}

	@DeleteMapping(value = "/listinfraction/{idInfraction}")
	public void deleteInfraction(@PathVariable(name = "idInfraction") Long idInfraction) {
		infractionRepository.deleteById(idInfraction);
	}

	// pdf
	@GetMapping(value = "/infractions/export/pdf")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<InputStreamResource> pdf() {
		// get user
		/*
		 * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 * Optional<User> user=userRepository.findByUsername(auth.getName());
		 * List<Reclamation> reclamations= (List<Reclamation>)
		 * reclamationRepository.findReclamationByUserId(user.get().getId());
		 */
		List<Infraction> infractions = (List<Infraction>) infractionRepository.findAll();
		ByteArrayInputStream bais = exportInfractionService.infpdf(infractions);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("content-Disposition", "inline;filename=infractions.pdf");
		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bais));
	}
}
