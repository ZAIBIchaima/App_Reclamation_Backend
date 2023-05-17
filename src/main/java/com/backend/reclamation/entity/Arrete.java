package com.backend.reclamation.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.Date;

import com.backend.reclamation.entity.Infraction;
import com.backend.security.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class Arrete {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long idArrete;
	private int  numArrete;
	//@Column (unique=true)
	private String  refArrete;
	private Date dateArrete;
	private String descriptionArrete;
	private String objetArrete;
	private int etat;
	//source Execution
	private int sourceExecution_Id; 
	//type decision
	private int typeDecision_Id;
	// Execution
	private String numExecution;
	private Date dateExecution;
	private String commentairesExecution;
	//bo
	private String numBoMinistre;
	private Date dateNumBoMinistre;
	private String commentairesMinistre;
	//private Temporal heureExecution;
	private Long userCreation;
	private Long userLastmodified;
	private Date dateUserCreation;
	private Date dateUserLastmodified;
	 
}
