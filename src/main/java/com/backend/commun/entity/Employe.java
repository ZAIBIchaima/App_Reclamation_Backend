package com.backend.commun.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.backend.reclamation.entity.Infraction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@Data
@NoArgsConstructor @ToString @AllArgsConstructor

public class Employe implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id @GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	private String nomPrenom;
	private Long contact;
	@Column(name="email", unique=true)
	private String email;
	private String fonction;
	private String adresse;
	private String libDep;
	private Long userCreation;
	private Long userLastmodified;
	private Date dateUserCreation;
	private Date dateUserLastmodified;
	
}
