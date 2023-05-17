package com.backend.reclamation.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Entite {
	@Id @GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	private String nom;
	private Long contact;
	@Column(name="email", unique=true)
	private String email;
	private String adresse;
	private String commentaires;
	private Long userCreation;
	private Long userLastmodified;
	private Date dateUserCreation;
	private Date dateUserLastmodified;
	
	public Entite() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Long getContact() {
		return contact;
	}

	public void setContact(Long contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	

	public String getCommentaires() {
		return commentaires;
	}

	public void setCommentaires(String commentaires) {
		this.commentaires = commentaires;
	}

	public Long getUserCreation() {
		return userCreation;
	}

	public void setUserCreation(Long userCreation) {
		this.userCreation = userCreation;
	}

	public Long getUserLastmodified() {
		return userLastmodified;
	}

	public void setUserLastmodified(Long userLastmodified) {
		this.userLastmodified = userLastmodified;
	}

	public Date getDateUserCreation() {
		return dateUserCreation;
	}

	public void setDateUserCreation(Date dateUserCreation) {
		this.dateUserCreation = dateUserCreation;
	}

	public Date getDateUserLastmodified() {
		return dateUserLastmodified;
	}

	public void setDateUserLastmodified(Date dateUserLastmodified) {
		this.dateUserLastmodified = dateUserLastmodified;
	}
	
	
	
	
	

}
