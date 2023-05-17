package com.backend.security.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.security.entity.ERole;
import com.backend.security.entity.Role;
import com.backend.security.entity.User;
import com.backend.security.payload.request.LoginRequest;
import com.backend.security.payload.request.SignUpRequest;
import com.backend.security.payload.response.JwtResponse;
import com.backend.security.payload.response.MessageResponse;
import com.backend.security.repository.RoleRepository;
import com.backend.security.repository.UserRepository;
import com.backend.security.security.jwt.JwtUtils;
import com.backend.security.security.services.UserDetailsImpl;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@GetMapping("/users")
	  public List<User> getAllUtilisateur() {
	    System.out.println("Get all Utilisateur...");
	 
	    List<User> Utilisateur = new ArrayList<>();
	    userRepository.findAll().forEach(Utilisateur::add);
	 
	    return Utilisateur;
	  }

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}
	@PutMapping("/users/{id}")

	public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User updatedUser) {
	    Optional<User> optionalUser = userRepository.findById(id);
	    
	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();
	        user.setUsername(updatedUser.getUsername());
	        user.setEmail(updatedUser.getEmail());
	        user.setPassword(encoder.encode(updatedUser.getPassword()));
	        
	        Set<Role> updatedRoles = updatedUser.getRoles();
	        Set<Role> roles = new HashSet<>();
	        for (Role role : updatedRoles) {
	            Role userRole = roleRepository.findByName(role.getName())
	                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	            roles.add(userRole);
	        }
	        user.setRoles(roles);
	        
	        userRepository.save(user);
	        return new ResponseEntity<User>(user, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
	    Optional<User> userData = userRepository.findById(id);

	    if (userData.isPresent()) {
	        userRepository.deleteById(id);
	        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
	    } else {
	        return ResponseEntity
	            .badRequest()
	            .body(new MessageResponse("Error: User not found."));
	    }
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUserById(@PathVariable("id") long id) {
	    Optional<User> userData = userRepository.findById(id);

	    if (userData.isPresent()) {
	        User user = userData.get();
	        return ResponseEntity.ok(user);
	    } else {
	        return ResponseEntity
	            .badRequest()
	            .body(new MessageResponse("Error: User not found."));
	    }
	
	
	}
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
	    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
	        return ResponseEntity
	                .badRequest()
	                .body(new MessageResponse("Error: Username is already taken!"));
	    }

	    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	        return ResponseEntity
	                .badRequest()
	                .body(new MessageResponse("Error: Email is already in use!"));
	    }

	    // Create new user's account
	    User user = new User(signUpRequest.getUsername(), 
	                         signUpRequest.getEmail(),
	                         encoder.encode(signUpRequest.getPassword())
	    		);

	    Set<ERole> selectedRoles = signUpRequest.getRoles();
	    Set<Role> roles = new HashSet<>();
	    for (ERole role : selectedRoles) {
	        Role userRole = roleRepository.findByName(role)
	                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	        roles.add(userRole);
	    }

	    user.setRoles(roles);
	    userRepository.save(user);

	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	@PostMapping("/signupp")
	public User createEmploye(@RequestBody User employe) {
		return userRepository.save(employe);
	}

	}

	



