package com.backend.reclamation.services;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
public class Const implements Serializable {

	/**
	*
	*/
	private static final long serialVersionUID = 1L;
	static String const1="République Tunisienne" ;
	static String const2="Ministère de l'Intérieur" ;

}
