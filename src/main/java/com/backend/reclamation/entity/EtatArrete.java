package com.backend.reclamation.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class EtatArrete {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;
	private String libArrete;
	private String couleur;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLibArrete() {
		return libArrete;
	}
	public void setLibArrete(String libArrete) {
		this.libArrete = libArrete;
	}
	public String getCouleur() {
		return couleur;
	}
	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	
	

}
