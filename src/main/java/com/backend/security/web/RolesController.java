package com.backend.security.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.security.entity.Role;
import com.backend.security.repository.RoleRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class RolesController {
	@Autowired
	RoleRepository roleRepository;
	@GetMapping("/roles")
	public List<Role> getAllRoles() {
	    System.out.println("Get all Roles...");
	    List<Role> Utilisateur = new ArrayList<>();
	    roleRepository.findAll().forEach(Utilisateur::add);
	 
	    return Utilisateur;
	  }
	@GetMapping("/roles/findbyName/{name}")
	public ResponseEntity<Role> getByName(@PathVariable(value = "name") String name) {

		Role Departement = roleRepository.findByName(name);
		return ResponseEntity.ok().body(Departement);

	}
}
