package com.backend.reclamation.web;

import java.io.ByteArrayInputStream;
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

import com.backend.reclamation.entity.Arrete;
import com.backend.reclamation.entity.Infraction;
import com.backend.reclamation.entity.Reclamation;
import com.backend.reclamation.repository.ArreteRepository;
import com.backend.reclamation.services.ExportArreteService;

@CrossOrigin(origins = "*")
@RestController
@PreAuthorize("hasRole('RECLAMATION') or hasRole('ADMIN')")
@RequestMapping("/api")
public class ArreteController {
	@Autowired
	private ArreteRepository arreteRepository;
	@Autowired
	public ExportArreteService exportArreteService;

	@GetMapping(value = "/listarrete")
	public List<Arrete> listArretes() {
		return arreteRepository.findAll();
	}

	@GetMapping(value = "/listarrete/{idArrete}")
	public Arrete listArretes(@PathVariable(name = "idArrete") Long idArrete) {
		return arreteRepository.findById(idArrete).get();
	}

	// recherche multi critere
	@GetMapping(value = "/filterArrete")
	public List<Arrete> filter(@RequestParam("dateStart") Date dateStart, @RequestParam("dateEnd") Date dateEnd,
			 @RequestParam("refArrete") String refArrete) {
		return arreteRepository.getArreteWithCustomQuery(dateStart, dateEnd, refArrete);
	}

	@PostMapping(value = "/listarrete")
	public Arrete createArrete(@RequestBody Arrete Arrete) {
		Arrete.setDateUserCreation(new Date());
		Arrete.setDateUserLastmodified(null);
		return arreteRepository.save(Arrete);
	}

	@PutMapping(value = "/listarrete/{idArrete}")
	public Arrete updateArrete(@PathVariable(name = "idArrete") Long idArrete, @RequestBody Arrete Arrete) {
		// System.out.println("ID Arrete :: "+idArrete);
		Arrete.setDateUserLastmodified(new Date());
		Arrete.setDateExecution(Arrete.getDateExecution());
		return arreteRepository.save(Arrete);
	}

	@DeleteMapping(value = "/listarrete/{idArrete}")
	public void deleteArrete(@PathVariable(name = "idArrete") Long idArrete) {
		arreteRepository.deleteById(idArrete);
	}

	@GetMapping(value = "/arretes/export/pdf")
	public ResponseEntity<InputStreamResource> pdf()  {
		List<Arrete> arretes = (List<Arrete>) arreteRepository.findAll();
		ByteArrayInputStream bais = exportArreteService.arretepdf(arretes);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("content-Disposition", "inline;filename=arretes.pdf");
		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bais));
	}

	@GetMapping(value = "/court/export/pdf/{idArrete}")
	public ResponseEntity<InputStreamResource> courtpdf(@PathVariable(name = "idArrete") Long idArrete) {
		System.out.println("ID Arrete :: " + idArrete);
		Arrete arretes = arreteRepository.findById(idArrete).get();
		System.out.println(" Arrete By ID  :: " + arretes);
		ByteArrayInputStream bais = exportArreteService.courtpdf(arretes);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("content-Disposition", "inline;filename=court.pdf");
		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bais));
	}

	@GetMapping(value = "/filterArreteByDate")
	public List<Arrete> filterArreteByDate(@RequestParam("dateArreteStart") Date dateArreteStart,
			@RequestParam("dateArreteEnd") Date dateArreteEnd) {
		return arreteRepository.findByDateArreteBetween(dateArreteStart, dateArreteEnd);
	}

	@GetMapping("/arrete/numArrete")
	public ResponseEntity<List<Arrete>> getArreteByNumAndNom(@RequestParam("numArrete") int numArrete,
			@RequestParam("refArrete") String refArrete) {
		return new ResponseEntity<List<Arrete>>(arreteRepository.findByNumArreteAndRefArrete(numArrete, refArrete),
				HttpStatus.OK);
	}

}
