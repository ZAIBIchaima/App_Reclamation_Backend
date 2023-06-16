package com.backend.reclamation.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.reclamation.entity.Infraction;
import com.backend.reclamation.entity.Reclamation;
import com.backend.reclamation.repository.ReclamationRepository;
import com.backend.reclamation.services.ExportReclamationService;
import com.backend.reclamation.services.shared.StorageUploadfileService;
import com.backend.security.entity.User;
import com.backend.security.repository.UserRepository;
import com.lowagie.text.DocumentException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;

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
	@Autowired
	StorageUploadfileService convertjson;

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
	public List<Reclamation> filter(@RequestParam("dateStart") Date dateStart, @RequestParam("dateEnd") Date dateEnd,
			@RequestParam("refReclamation") String refReclamation) {

		System.out.println("Filter :::: " + dateStart + dateEnd);
		return reclamationRepository.getReclamationWithCustomQuery(dateStart, dateEnd, refReclamation);
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

	// search

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

	// rapport

	// pdf itext
	@GetMapping(value = "/report/pdf")
	public ResponseEntity<InputStreamResource> pdfReclamationReport(
			@RequestParam(value = "startDate", required = false) Date startDate,
			@RequestParam(value = "endDate", required = false) Date endDate,
			@RequestParam(value = "refReclamation", required = false) String refReclamation) throws MalformedURLException, IOException {
		
		List<Reclamation> reclamations;
		
		if (startDate == null || endDate == null) {
			reclamations = (List<Reclamation>) reclamationRepository.findAll();
		} else {
			reclamations =  reclamationRepository.getReclamationWithCustomQuery(startDate, endDate, refReclamation);
		}
		
		
		ByteArrayInputStream bais = exportReclamationService.reclamationPdf(reclamations);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("content-Disposition", "inline;filename=reclamations.pdf");
		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bais));
	}
	
	
	
	@GetMapping(value = "/export/pdf")
	public ResponseEntity<InputStreamResource> pdfReclamation() throws MalformedURLException, IOException {
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

	//pdf jasper
	@GetMapping("/ReportList")
	public void getReportList(HttpServletResponse response,
			@RequestParam(value = "startDate", required = false) Date startDate,
			@RequestParam(value = "endDate", required = false) Date endDate,
			@RequestParam(value = "refReclamation", required = false) String refReclamation)
			throws SQLException, JRException, IOException, DocumentException, FontFormatException {
		final InputStream inputStream = this.getClass().getResourceAsStream("/reports/reclamation_List.jrxml");
		List<Reclamation> list;
		if (startDate == null || endDate == null) {
			list = reclamationRepository.findAll();
		} else {
			list = reclamationRepository.getReclamationWithCustomQuery(startDate, endDate, refReclamation);
		}
		if (list.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No records found for the specified date range.");
		}
		InputStream is = new ByteArrayInputStream(convertjson.convertToJson(list).getBytes());
		JasperReport report = JasperCompileManager.compileReport(inputStream);

		HashMap<String, Object> params = new HashMap<String, Object>();
		JRDataSource beanColDataSource = new JsonDataSource(is);
		params.put("FILE_FORMAT", "pdf");
		params.put("SUBREPORT_DIR", "reports/");

		JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, beanColDataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=Reclamation.pdf");
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}

}
