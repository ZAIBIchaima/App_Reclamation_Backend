package com.backend.reclamation.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.geo.Point;

import com.backend.commun.entity.Employe;
import com.backend.security.entity.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @ToString

public class Reclamation {	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (unique=true)
	private String refReclamation;
	private int numReclamation;	
	private Date dateReclamation;
	private String prenomNomSourceReclamation;
	private String adresseSourceReclamation;
	private String prenomNomSourceDestinataire;
	private String adresseSourceDestinataire;
	private String adresseLocal;
	private String objetifReclamation;
	private String observation;
	private int etat;
	private Long userCreation;
	private Long userLastmodified;
	private Date dateUserCreation;
	private Date dateUserLastmodified;
	
}
