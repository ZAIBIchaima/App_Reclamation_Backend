package com.backend.reclamation.entity;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.geo.Point;

import com.backend.commun.entity.Employe;
import com.backend.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class Infraction implements Serializable{
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long idInfraction;
	@Column (unique=true)
	private Long  numInfraction;
	//@Column (unique=true)
	//private String refInfraction;
	private Date dateInfraction;
	//@Temporal(TemporalType.TIME)
	//private LocalDateTime heureInfraction;
	//@Column (unique=true)
	//private int cinSourceInfraction;
	private String descriptionInfraction;
	private String niveauTraveaux;
	private String degats;
	private String descriptions;
	//source 
	private String source;
	private int etat;
	private Long userCreation;
	private Long userLastmodified;
	private Date dateUserCreation;
	private Date dateUserLastmodified;
	//reclamation
	private int numReclamation; 
	//employee
	private String codeEmploye;

}
