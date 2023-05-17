package com.backend.security.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.backend.reclamation.entity.Arrete;
import com.backend.reclamation.entity.Infraction;
import com.backend.reclamation.entity.Reclamation;

import lombok.Data;

@Data
@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {
		
	}
	public void grantAuthority(ERole authority) {
        if ( roles == null ) {
        	ArrayList<ERole> roles = new ArrayList<>();
        roles.add(authority);}
    }
	public User(  String username, String email, String password) {
		super();
	
	
		this.username = username;
		this.email = email;
		this.password = password;
		
	}
	}


